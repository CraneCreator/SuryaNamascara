package com.namascara.surya

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.RingtoneManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

// переменная для хранения состояния "ручная/автоматическая перемотка"
// ключ задан константой, чтобы не ошибиться при ручном наборе имени переменной в коде
const val KEY_SCROLL_STATE = "userScrollChangeState"

// переменная типа String для хранения таймера автоскроллинга в тысячных долях секунды
// const для sharedPreferences, ИМЯ КЛЮЧА ("TIMER_MILLIS_PREFER_KEY") и ЗНАЧЕНИЕ
const val TIMER_MILLIS_PREFER_KEY = "TIMER_MILLIS_PREFER_KEY"

const val PROGRESS_BAR_MAX = 200

class MainActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private var viewPager2: ViewPager2? = null

    private var viewPager2Adapter: ViewPager2Adapter? = null

    private var userScrollChange = true

    private var framePosition: Int = 0

    private var timerBtn: ImageButton? = null

    private var progressBar: ProgressBar? = null

    private var progressCount = 0

    // переменная типа SharedPreferences для хранения настроек приложения после выхода из него, задано начальное значение null
    private var sharedPref: SharedPreferences? = null

    // private val bellSound: MediaPlayer? = MediaPlayer.create(this, R.raw.bell_sound)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO ??? созданная ранее переменная sharedPref получает значение из контекста, но не очень понимаю, как это происходит
        sharedPref = getPreferences(Context.MODE_PRIVATE)
        // если функция getTimerMillis возвращает -1 (а выше определено, что -1 вернётся,
        // если sharedPref равно null,т.е. в Preferences ничего ещё не записано
        // (на момент создания Activity), при первом запуске приложения, например.
        if (getTimerMillis() == -1L) {
            // то в Preferences по ключу-константе заносится значение по умолчанию, 10 секунд
            sharedPref?.edit()?.putLong(TIMER_MILLIS_PREFER_KEY, 10000L)?.apply()
        }

        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.viewpager)

        viewPager2Adapter = ViewPager2Adapter(this)

        viewPager2?.adapter = viewPager2Adapter

        val wormDotsIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        viewPager2?.let { wormDotsIndicator.attachTo(it) }

        timerBtn = findViewById(R.id.timer_tune_button)
        timerBtn?.setOnClickListener {
            SuraDialogFragment().show(supportFragmentManager, "My Fragment")
        }

        progressBar = findViewById(R.id.progress_bar)

        // Viewpager2 - более продвинутая версия RecyclerView, для работы с элементами адаптера
        viewPager2?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                framePosition = position
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    userScrollChange = true
                    handleUserScrollChange()
                }
            }
        })

        if (savedInstanceState != null) {
            userScrollChange = savedInstanceState.getBoolean(KEY_SCROLL_STATE, false)
        }
        // здесь эта функция вызывается, чтобы приложение сразу ПОСЛЕ ЗАПУСКА оказалось в состоянии, как будто юзер провел пальцем
        handleUserScrollChange()
    }

    override fun onPause() {
        super.onPause()
        userScrollChange = true
        // здесь эта функция вызывается, чтобы приложение ПОСЛЕ ПАУЗЫ оказалось в состоянии, как будто юзер провел пальцем
        handleUserScrollChange()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_SCROLL_STATE, userScrollChange)
    }

    private fun progressBarShow() {
        handler.postDelayed(
            {
                progressCount += 1
                progressBar!!.progress = progressCount % PROGRESS_BAR_MAX
                if (progressCount % PROGRESS_BAR_MAX == 0) {
                    soundPlay()
                    handleAutoScrollChange()
                }
                if (!userScrollChange) {
                    progressBarShow()
                }
            },
            getTimerMillis() / PROGRESS_BAR_MAX
        )
    }

    private fun soundPlay() {
        try {
            RingtoneManager.getRingtone(
                applicationContext,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            )
                .play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleAutoScrollChange() {
        // если показываемая поза не последняя в списке, то показать следующую
        Log.d("MY_TAG", "handleAutoScrollChange() IF: framePosition = $framePosition, viewPager2Adapter?.itemCount = $viewPager2Adapter?.itemCount")
        if (framePosition < (viewPager2Adapter?.itemCount ?: 0) - 1) {
            viewPager2?.setCurrentItem(framePosition + 1, true)
        } else {
            // иначе поменять название кнопки AutoScroll на Restart
            Log.d("MY_TAG", "handleAutoScrollChange() ELSE: framePosition = $framePosition, viewPager2Adapter?.itemCount = $viewPager2Adapter?.itemCount")
            onLastFrameAutoscrollButtonNameIsRestart()
            // и остановиться на последней позе, далее перейти в режим ручного скроллинга
            userScrollChange = true
            handleUserScrollChange()
        }
    }


    private fun handleUserScrollChange() {
        // функция срабатывает лишь один раз, когда нажимается "AutoScroll",
        // и далее во время автоскролла не работает.
        // но срабатывает каждый раз, когда юзер проводит пальцем

        // если показана последняя поза, поменять название кнопки AutoScroll на Restart
        Log.d("MY_TAG", "handleUserScrollChange(): framePosition = $framePosition")
        if (framePosition > (viewPager2Adapter?.itemCount ?: 0) - 2)
            onLastFrameAutoscrollButtonNameIsRestart()
        else
        // иначе название кнопки должно быть AutoScroll
            onNotLastFrameAutoscrollButtonNameIsAutoscrollButton()

        handleProgressBarVisibility()
        handleAutoScrollButton()
    }

    private fun onLastFrameAutoscrollButtonNameIsRestart() {
        val autoScrollButton: Button = findViewById(R.id.auto_scroll_btn)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            autoScrollButton.text =
                getString(R.string.autoscroll_button_name_in_end_of_list_vert)
        else
            autoScrollButton.text =
                getString(R.string.autoscroll_button_name_in_end_of_list_hori)
    }

    private fun onNotLastFrameAutoscrollButtonNameIsAutoscrollButton() {
        val autoScrollButton: Button = findViewById(R.id.auto_scroll_btn)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            autoScrollButton.text =
                getString(R.string.auto_scroll_btn_text_vert)
        else
            autoScrollButton.text =
                getString(R.string.auto_scroll_btn_text_hori)
    }

    private fun handleProgressBarVisibility() {
        progressBar!!.visibility = if (userScrollChange)
            INVISIBLE
        else
            VISIBLE
    }

    private fun handleAutoScrollButton() {
        Log.d("MY_TAG", "handleAutoScrollButton() WORKS: framePosition = $framePosition")
        val autoScrollButton: Button = findViewById(R.id.auto_scroll_btn)
        val timerTuneButton: ImageButton = findViewById(R.id.timer_tune_button)
        if (userScrollChange) {
            autoScrollButton.visibility = VISIBLE
            timerTuneButton.visibility = VISIBLE
            autoScrollButton.setOnClickListener {
                userScrollChange = false
                progressCount = 0
                progressBarShow()
                Log.d("MY_TAG", "handleAutoScrollButton() BEFORE handleUserScrollChange(): framePosition = $framePosition")
                handleUserScrollChange()
                // TODO if current page is last one, restart from first pose
                if (framePosition == (viewPager2Adapter?.itemCount ?: 0) - 1) {
                    framePosition = -2
                    handleAutoScrollChange()
                }
            }
        } else {
            autoScrollButton.visibility = INVISIBLE
            timerTuneButton.visibility = INVISIBLE
            autoScrollButton.setOnClickListener(null)
        }
    }

    // функция вынесена вне других функций. Возвращает -1, если в "sharedPref" null. Иначе считывает из "sharedPref" данные по ключу.
    // или опять-таки возвращает -1 по умолчанию, если данные по ключу считать не удалось
    private fun getTimerMillis(): Long {
        val sp = sharedPref ?: return -1
        return sp.getLong(TIMER_MILLIS_PREFER_KEY, -1)
    }
}


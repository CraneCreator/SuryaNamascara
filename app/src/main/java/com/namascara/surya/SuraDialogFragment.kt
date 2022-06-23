package com.namascara.surya

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

// это спец.класс типа "DialogFragment" создаётся, чтобы показать юзеру диалог
// (панель с вопросом и кнопками ответов), но не вызывать для этого ещё одну Activity.
// а для реагарования на нажатия кнопок добавлен "View.OnClickListener" в запрашиваемые параметры
class SuraDialogFragment : androidx.fragment.app.DialogFragment(), View.OnClickListener {
    // переменная timerValue будет запоминать число, введенное юзером в поле ввода (в формате EditText)
    private var timerValue: EditText? = null

    // функция для создания View-элемента, собственно диалога с юзером, по шаблону типа "dialog_fragment"
    override fun onCreateView(
        // TODO ??? откуда потом программа берёт следующие три параметра, я не очень понял
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // вызов суперкласса с параметрами, полученными из "fun onCreateView" (сразу выше)
        super.onCreateView(inflater, container, savedInstanceState)

        // переменная "suraDialogFragmentLayout" типа View получает значение из шаблона "dialog_fragment.xml"
        val suraDialogFragmentLayout = inflater.inflate(R.layout.dialog_fragment, container, false)
        // в фрагменте всего одно поле для ввода цифр и две кнопки. Взаимодействие с этими элементами нужно описать:
        handleDialogFragmentEditTimer(suraDialogFragmentLayout)
        // назначение "слушателей" на две кнопки - confirm & cancel
        suraDialogFragmentLayout.findViewById<View>(R.id.button_confirm).setOnClickListener(this)
        suraDialogFragmentLayout.findViewById<View>(R.id.button_cancel).setOnClickListener(this)
        return suraDialogFragmentLayout
    }

    override fun onClick(v: View?) {
        Log.d("MY_TAG", "DialogFragment: " + (v as Button).text)
        when (v.text) {
            "Confirm" -> handleConfirm()
            "Cancel" -> Log.d("MY_TAG", "CANCEL! ${timerValue?.text}")
        }
        dismiss()
    }

    private fun handleDialogFragmentEditTimer(suraDialogFragmentLayout: View) {
        // привязка переменной timerValue к объекту того же типа "edit_text" - edit_timer в фрагменте
        timerValue = suraDialogFragmentLayout.findViewById(R.id.edit_timer)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) {
            timerValue?.setText((sharedPref.getLong(TIMER_MILLIS_PREFER_KEY, -1) / 1000).toString())
        }
    }

    // функция для обработки нажатия на кнопку "confirm" в DialogFragment
    private fun handleConfirm() {
        Log.d("MY_TAG", "CONFIRM! ${timerValue?.text}")
        // переменная sharedPref выясняет состояние Preferences для следующей сессии
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        // если в Preferences уже что-то записано
        if (sharedPref != null) {
            // TODO ??? примерно понимаю, что редактируется переменная "sharedPref",
            // но что такое "with" и зачем оно здесь, так и не понял, вроде "with" для множественных операций
            // вычитал, что with запускает код в лямбде, но ничего не ждёт в ответ, просто "выполните этот код" ???
            with(sharedPref.edit()) {
                // в timerMillis записывается число секунд, введённое юзером в поле "timerValue"
                val timerMillis = timerValue?.text.toString().toLong() * 1000
                // команда putLong исп. только для "sharedPreferences", вписывает данные по ключу и должна заканчиваться .apply()
                putLong(TIMER_MILLIS_PREFER_KEY, timerMillis).apply()
                Log.d("MY_TAG2", "$timerMillis SharedPreferences data written to SuraSettings!")
            }
        }
    }
}
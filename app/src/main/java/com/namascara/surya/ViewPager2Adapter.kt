package com.namascara.surya

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

internal class ViewPager2Adapter(private val ctx: Context) : RecyclerView.Adapter<AsanaHolder>() {
    // This method returns our layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsanaHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.asana_holder, parent, false)
        return AsanaHolder(view)
    }

    // This method binds the screen with the view
    override fun onBindViewHolder(holder: AsanaHolder, position: Int) {
        // This will set the images in imageview
        holder.holdAsansImage.setImageResource(asanaLists[position].asanaImage)
        holder.holdAsansText.text = asanaLists[position].asanaText
        holder.posNumber.text = (position + 1).toString()
    }

    // This Method returns the size of the Array
    override fun getItemCount(): Int {
        return asanaLists.size
    }

    private val asanaLists = listOf(
        Asana(
            R.drawable.sura_01,
            "Ом МиТРаайа намаха\n" +
                    "ॐ िमत्राय नमः\n" +
                    "[oṃ mitrāya namaḥ]\n" +
                    "Аспект дружелюбия."
        ),
        Asana(
            R.drawable.sura_02,
            "Ом Равайе намаха\n" +
                    "ॐ रवये नमः\n" +
                    "[oṃ ravaye namaḥ]\n" +
                    "Аспект разрушения."
        ),
        Asana(
            R.drawable.sura_03,
            "Ом Суурьяайа намаха\n" +
                    "ॐ सूर्याय  नमः\n" +
                    "[oṃ sūryaya namaḥ]\n" +
                    "Аспект творения."
        ),
        Asana(
            R.drawable.sura_04,
            "Ом Бхаанаве намаха\n" +
                    "ॐ भानवे नमः\n" +
                    "[oṃ bhānave namaḥ]\n" +
                    "Аспект создания света."
        ),
        Asana(
            R.drawable.sura_05,
            "Ом Бхаагаайа намаха\n" +
                    "ॐ भगाय नमः\n" +
                    "[oṃ bhāgāya namaḥ]\n" +
                    "Тот, кто делит, распределяет."
        ),
        Asana(
            R.drawable.sura_06,
            "Ом Пушне намаха    \n" +
                    "ॐ पुष्णे  नमः\n" +
                    "[oṃ puṣṇe namaḥ]\n" +
                    "Аспект создания пищи."
        ),
        Asana(
            R.drawable.sura_07,
            "Ом Хираньягарбхаайа намаха\n" +
                    "ॐ िहरण्यगर्भाय  नमः\n" +
                    "[oṃ hiraṇyagarbhāya\n" +
                    "namaḥ]\n" +
                    "Аналогия Солнца с Золотым зародышем, из которого, согласно индийской мифологии,\n" +
                    "был создан мир."
        ),
        Asana(
            R.drawable.sura_08,
            "Ом Мариичайе намаха\n" +
                    "ॐ मरीचये नमः\n" +
                    "[oṃ marīcaye namaḥ]\n" +
                    "Аспект смерти."
        ),
        Asana(
            R.drawable.sura_09,
            "Ом Аадитьяайа намаха\n" +
                    "ॐ आिदत्याय नमः\n" +
                    "[oṃ ādityāya namaḥ]\n" +
                    "Адитья (неделимая), согласно индийской мифологии, является матерью Солнца.\n" +
                    "В данной мантре использован матроним Солнца."
        ),
        Asana(
            R.drawable.sura_10,
            "Ом СавиТРе намаха\n" +
                    "ॐ सिवत्रे नमः\n" +
                    "[oṃ savītre namaḥ]\n" +
                    "Аспект творения."
        ),
        Asana(
            R.drawable.sura_11,
            "Ом Аркаайа намаха\n" +
                    "ॐ अर्काय  नमः\n" +
                    "[oṃ arkāya namaḥ]\n" +
                    "Солнце как объект поклонения."
        ),
        Asana(
            R.drawable.sura_12,
            "Ом Бхааскаараайа намаха\n" +
                    "ॐ भास्काराय नमः\n" +
                    "[oṃ bhāskārāya namaḥ]\n" +
                    "Аспект создания света"
        ),
        Asana(
            R.drawable.sura_13,
            "Ом МиТРаайа намаха\n" +
                    "ॐ िमत्राय नमः\n" +
                    "[oṃ mitrāya namaḥ]\n" +
                    "Аспект дружелюбия."
        )
    )
}
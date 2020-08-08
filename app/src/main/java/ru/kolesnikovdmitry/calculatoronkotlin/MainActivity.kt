package ru.kolesnikovdmitry.calculatoronkotlin

/*
~Используется библиотека exp4j - Библиотке для java для рассчета математических выражений.
С ее помощью можно даже вычислять тангенсы и прочее.
в gradle прописываем зависимость:
implementation 'net.objecthunter:exp4j:0.4.8'   ~~ не забудьте указать лицензию в своем приложении!

~Чтобы сделать изменение цвета кнопки при нажатии на нее, не обязательно прописывать onTouchListener!
Можно просто сделать тему кнопки с параметром <item name="colorControlHighlight">#991199</item> -
цвет при нажатии.
Важно напомнить, что именно тема, а не стиль. Стили вы прописываете уже как вам надо, а эта штука
работает только как тема!
А еще работает только если вы не используете атрибут Background для своей кнопки, а используете
BackgroundTint!

*/

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    //Строка уравнения
    private var stringOfEquation = ""
    //массив для скобочной последовательности
    private var arrOfBrackets = ""
    //массив для истории
    private var history : Array<String> = emptyArray()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Устанавливаем слушатели на нажатие кнопки на наши цифры и кнопки операций
        textViewNumber0.setOnClickListener {onClickActMain(textViewNumber0)}
        textViewNumber1.setOnClickListener {onClickActMain(textViewNumber1)}
        textViewNumber2.setOnClickListener {onClickActMain(textViewNumber2)}
        textViewNumber3.setOnClickListener {onClickActMain(textViewNumber3)}
        textViewNumber4.setOnClickListener {onClickActMain(textViewNumber4)}
        textViewNumber5.setOnClickListener {onClickActMain(textViewNumber5)}
        textViewNumber6.setOnClickListener {onClickActMain(textViewNumber6)}
        textViewNumber7.setOnClickListener {onClickActMain(textViewNumber7)}
        textViewNumber8.setOnClickListener {onClickActMain(textViewNumber8)}
        textViewNumber9.setOnClickListener {onClickActMain(textViewNumber9)}
        textViewOperationDivision.setOnClickListener { onClickActMain(textViewOperationDivision) }
        textViewOperationEqual.setOnClickListener    { onClickActMain(textViewOperationEqual)    }
        textViewOperationMinus.setOnClickListener    { onClickActMain(textViewOperationMinus)    }
        textViewOperationMultiply.setOnClickListener { onClickActMain(textViewOperationMultiply) }
        textViewOperationPlus.setOnClickListener     { onClickActMain(textViewOperationPlus)     }
        textViewDelete.setOnClickListener {onClickActMain(textViewDelete)}
        textViewAC.setOnClickListener { onClickActMain(textViewAC) }
        textViewPoint.setOnClickListener { onClickActMain(textViewPoint) }
        textViewLeftBracket.setOnClickListener { onClickActMain(textViewLeftBracket) }
        textViewRightBracket.setOnClickListener { onClickActMain(textViewRightBracket) }

        //Долгим нажатием на кнопку удаляем все элементы
        textViewDelete.setOnLongClickListener {
            return@setOnLongClickListener deleteAllElements()
        }
    }

    //Удаляет все элементы и все строки
    private fun deleteAllElements(): Boolean {
        //анимация для удаления элементов:
        //.x(1500f) - перемещает на 1500 по оси х, аналогично по y
        scrollViewAnsField.animate()
            .x(1500f).duration = 500

        scrollViewEqField.animate()
            .x(-1500f).duration = 500

        scrollViewAnsField.postDelayed({
            textViewAnsField.text = ""
            scrollViewAnsField.animate().x(0f).duration = 0
        }, 500)

        scrollViewEqField.postDelayed({
            textViewEqField.text = ""
            scrollViewEqField.animate().x(0f).duration = 0
        }, 500)

        stringOfEquation = ""
        arrOfBrackets = ""

        return true
    }

    //Добавляет элемент
    private fun addElement(str : String) {
        stringOfEquation += str
        textViewEqField.text = stringOfEquation
    }

    //Удаляет элемент с учетом возможного удаления скобочки
    private fun delElement() {
        if(stringOfEquation.isNotEmpty()) {
            if(stringOfEquation[stringOfEquation.length - 1] == '(') {
                arrOfBrackets.dropLast(1)
            }
            if(stringOfEquation[stringOfEquation.length - 1] == ')') {
                arrOfBrackets += '('
            }
            stringOfEquation = stringOfEquation.dropLast(1)
            textViewEqField.text = stringOfEquation
            if (!haveEqManyNumbers()) {
                textViewAnsField.text = ""
            }
            printPreResult()
        }
    }

    //функция, в которой обрабатываюьтся нажатия на кнопку
    private fun onClickActMain(view: View) {
        when(view.id) {
            R.id.textViewNumber0 -> {
                addElement("0")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber1 -> {
                addElement("1")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber2 -> {
                addElement("2")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber3 -> {
                addElement("3")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber4 -> {
                addElement("4")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber5 -> {
                addElement("5")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber6 -> {
                addElement("6")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber7 -> {
                addElement("7")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber8 -> {
                addElement("8")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewNumber9 -> {
                addElement("9")
                scrollToEnd(scrollViewEqField)
                printPreResult()
            }
            R.id.textViewDelete -> {
                delElement()
                printPreResult()
            }
            R.id.textViewOperationMinus ->    {
                addMinus()
                scrollToEnd(scrollViewEqField)
            }
            R.id.textViewOperationDivision -> {
                addDivider()
                scrollToEnd(scrollViewEqField)
            }
            R.id.textViewOperationMultiply -> {
                addMultiply()
                scrollToEnd(scrollViewEqField)
            }
            R.id.textViewOperationPlus ->     {
                addPlus()
                scrollToEnd(scrollViewEqField)
            }
            R.id.textViewLeftBracket ->       {
                addOpenBracket()
                scrollToEnd(scrollViewEqField)
            }
            R.id.textViewRightBracket ->      { if (canAddCloseBracket(stringOfEquation)) {
                addCloseBracket()
                scrollToEnd(scrollViewEqField)
            } }
            R.id.textViewAC -> { deleteAllElements() }
            R.id.textViewPoint -> {
                if (!haveNumberPoint(stringOfEquation)) {
                    addElement(".")
                    scrollToEnd(scrollViewEqField)
                    printPreResult()
                }
            }
            R.id.textViewOperationEqual -> {
                if (textViewAnsField.text == "") return
                stringOfEquation = removeUselessOperands(stringOfEquation)
                stringOfEquation = addBracketsIfNeeds(stringOfEquation)
                var tmpStrForHistory = "$stringOfEquation = "
                val result = solveTheEquation()
                if(result != "Bad expression!") {
                    tmpStrForHistory += result
                    history += tmpStrForHistory
                    //textViewEqField.text = result
                    //анимация перехода
                    startAnimationFlyAway()
                }
                else {
                    textViewEqField.text = result
                }
            }
        }
    }

    //Это анимации перелета тестка из нижнего поля в верхнее
    private fun startAnimationFlyAway() {
        //ИЛи сделать более красиво с помощью метода animate():
        scrollViewAnsField.animate()
            .setDuration(200)
            .setInterpolator(FastOutSlowInInterpolator())
            .y(0f)

        scrollViewEqField.animate()
            .setDuration(200)
            .setInterpolator(FastOutSlowInInterpolator())
            .y(-350f)

        textViewEqField.postDelayed({
            //Возвращаем на прежнее место, то есть перемещаем на y()
            scrollViewEqField.animate().y(0f).duration = 0
            textViewEqField.text = stringOfEquation
        }, 200)
        textViewAnsField.postDelayed({
            //Возвращаем скроллвьшку в исходное положение
            scrollViewAnsField.animate().y(350f).duration = 0
            textViewAnsField.text = ""
        }, 200)
    }

    //Эта функция печатает ответ в textViewAnsField каждый раз после нажатия какой-либо клавиши
    private fun printPreResult() {
        if(!haveEqManyNumbers()) return
        try {
            var curStringOfEq =  stringOfEquation
            curStringOfEq = removeUselessOperands(curStringOfEq)
            curStringOfEq = addBracketsIfNeeds(curStringOfEq)
            val expressionBuilder = ExpressionBuilder(curStringOfEq).build()
            val result = expressionBuilder.evaluate()
            //проверим есть есть ли точка в числе:
            val resLong = result.toLong()
            val tmpResult = if (result == resLong.toDouble()) {
                //textViewAnsField.text = resLong.toString()
                resLong.toString()
            } else {
                //textViewAnsField.text = result.toString()
                result.toString()
            }
            textViewAnsField.text = tmpResult
        } catch (th: Throwable) {
            textViewAnsField.text = "WTF MAN???"
        }

    }

    //Функция проверяет текущую строку уравнения и выводит True, если есть больше одного числа в строке
    private fun haveEqManyNumbers(): Boolean {
        if (stringOfEquation.isEmpty()) return false
        var curPosition = stringOfEquation.length - 1
        while (curPosition >= 0 && (stringOfEquation[curPosition] == '+' ||
                    stringOfEquation[curPosition] == '-' || stringOfEquation[curPosition] == '*' ||
                    stringOfEquation[curPosition] == '/' || stringOfEquation[curPosition] == ')' ||
                    stringOfEquation[curPosition] == '(')) {
            curPosition -= 1
        }
        if (curPosition <= 0) return false
        while (curPosition >= 0 && (stringOfEquation[curPosition] in '0'..'9' ||
                    stringOfEquation[curPosition] == '.')) {
            curPosition -= 1
        }
        if (curPosition <= 0) return false
        while (curPosition >= 0 && (stringOfEquation[curPosition] == '+' ||
                    stringOfEquation[curPosition] == '-' || stringOfEquation[curPosition] == '*' ||
                    stringOfEquation[curPosition] == '/' || stringOfEquation[curPosition] == ')' ||
                    stringOfEquation[curPosition] == '(')) {
            curPosition -= 1
        }
        if (curPosition < 0) return false
        return true
    }

    //функция для корректного отображения поля ввода
    private fun scrollToEnd(scrollView : HorizontalScrollView) {
        scrollView.post(Runnable { scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT) })
        scrollView.postDelayed(Runnable { scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT) }, 1000)
    }

    //Добавляет к переданной строке закрывающие скобки и возвращает новую строку со скобками
    private fun addBracketsIfNeeds(strVal : String) : String{
        if (arrOfBrackets.isEmpty()) return strVal
        var str = strVal
        var curArrOfBrackets = arrOfBrackets
        var curPos = curArrOfBrackets.length - 1
        while (curPos >= 0 && curArrOfBrackets[curPos] == '(') {
            str += ')'
            //textViewEqField.text = stringOfEquation
            curArrOfBrackets = curArrOfBrackets.dropLast(1)
            curPos--
        }
        return str
    }

    //Добавляет открывающую скобку к строке, если можно, иначе ничего не делает
    private fun addOpenBracket() {
        arrOfBrackets += '('
        stringOfEquation += '('
        textViewEqField.text = stringOfEquation
    }

    //Добавляет Закрывающую скобку к stringOfEquation, если можно, иначе ничего не делает
    private fun addCloseBracket() {
        //если скобочная последовательность правильная, то нельзя добавлять закрывающую скобку
        if (arrOfBrackets.isEmpty()) return
        // если на вершине стека стоит закрывающая скобка - тоже нельзя открывать
        val curPos = arrOfBrackets.length - 1
        if(arrOfBrackets[curPos] == ')') return
        //если перед закрывающей скобкой стоит операнд, то тоже нельзя закрыть:
        if(stringOfEquation[stringOfEquation.length - 1] == '+' ||
            stringOfEquation[stringOfEquation.length - 1] == '-' ||
            stringOfEquation[stringOfEquation.length - 1] == '*' ||
            stringOfEquation[stringOfEquation.length - 1] == '/' ) return
        //иначе убираем из стека открывающую скобку и добавляем в выражение закрывающую
        arrOfBrackets = arrOfBrackets.dropLast(1)
        stringOfEquation += ')'
        textViewEqField.text = stringOfEquation
    }

    //Добавляет плюс к stringOfEquation, если можно
    private fun addPlus() {
        //сначала строки не добавляем
        if (stringOfEquation.isEmpty()) return
        //после операторов тоже не добавляем, а заменяем оператор
        val curPos = stringOfEquation.length - 1
        if (stringOfEquation[curPos] == '+' || stringOfEquation[curPos] == '-' ||
                stringOfEquation[curPos] == '/' || stringOfEquation[curPos] == '*') {
            stringOfEquation = stringOfEquation.dropLast(1)
            stringOfEquation += '+'
            textViewEqField.text = stringOfEquation
            return
        }
        //если открытая скобочка, то не ставим перед числом
        if (stringOfEquation[curPos] == '(') return
        //иначе ставим
        stringOfEquation += '+'
        textViewEqField.text = stringOfEquation
    }

    //Добавляет умножения к stringOfEquation, если можно
    private fun addMultiply() {
        //если строка пуста, то нельзя
        if (stringOfEquation.isEmpty()) return
        val curPos = stringOfEquation.length - 1
        //если в конце строки стоит оператор, то заменяем его на умножение
        if (stringOfEquation[curPos] == '/' || stringOfEquation[curPos] == '*' ||
                stringOfEquation[curPos] == '+' || stringOfEquation[curPos] == '-') {
            stringOfEquation = stringOfEquation.dropLast(1)
            stringOfEquation += '*'
            textViewEqField.text = stringOfEquation
            return
        }
        //если перед открывающей скобкой, то не добавляем
        if(stringOfEquation[curPos] == '(') return
        //иначе добавляем
        stringOfEquation += '*'
        textViewEqField.text = stringOfEquation
        return
    }

    //Добавляет знак деления к stringOfEquation, если можно
    private fun addDivider() {
        //если строка еще пустая, то ничего не добавляем
        if (stringOfEquation.isEmpty()) return
        val curPos = stringOfEquation.length - 1
        //если текущий символ - операнд, то удаляет его и на его место ставит себя
        if(stringOfEquation[curPos] == '/' || stringOfEquation[curPos] == '+' ||
                stringOfEquation[curPos] == '-' || stringOfEquation[curPos] == '*') {
            stringOfEquation = stringOfEquation.dropLast(1)
            stringOfEquation += '/'
            textViewEqField.text = stringOfEquation
            return
        }
        //если спереди стоит открывающая скобка, то тоже нельзя
        if (stringOfEquation[curPos] == '(') return
        //иначе просто добавляем его
        stringOfEquation += '/'
        textViewEqField.text = stringOfEquation
    }

    //Добавляет минус к stringOfEquation, если можно
    private fun addMinus() {
        val curPosition = stringOfEquation.length - 1
        //если перед новым минусом уже стоит минус, то не добавляем
        if (stringOfEquation.isNotEmpty() && stringOfEquation[curPosition] == '-') return
        // если перед новым минусом стоит знак +, то он меняется на минус
        if (stringOfEquation.isNotEmpty() && stringOfEquation[curPosition] == '+') {
            stringOfEquation = stringOfEquation.dropLast(1)
            stringOfEquation += '-'
            textViewEqField.text = stringOfEquation
            return
        }
        //в остальных случаях просто ставится минус
        stringOfEquation += "-"
        textViewEqField.text = stringOfEquation
    }

    //возвращает копию переданной строки без ненужных последних операндов
    private fun removeUselessOperands(strVal : String) : String {
        var str = strVal
        //если пустая строка, то ничего удалять не надо
        if (str.isEmpty()) return str
        // если строка кончается на какой-то операнд, то удаляем его
        while (str.isNotEmpty() && (str[str.length - 1] == '*' ||
            str[str.length - 1] == '/' ||
            str[str.length - 1] == '+' ||
            str[str.length - 1] == '-')) {
            str = str.dropLast(1)
        }
        return str
    }

    //Проверяет, можно ли добавить закрывающую скобку к переданной строке и возвращает да, если можно
    private fun canAddCloseBracket(str : String): Boolean {
        //сначала строки нельзя
        if (str.isEmpty()) return false
        val size = str.length
        val curPosition = size - 1
        //если сразу перед закрывающей стоит открывающая скобка, то есть пустое тело
        if(str[curPosition] == '(') return false
        //Если это нарушает скобочную последовательность
        return true
    }

    //Функция возвращает строку - ответ, если все решено, "Bad expression!" - если возникла ошибка,
    //и "Empty expression!", если текущее выражение пустое
    private fun solveTheEquation() : String {
        if (stringOfEquation.isEmpty()) {
            return "Empty expression!"
        }
        try {
            val expressionBuilder = ExpressionBuilder(stringOfEquation).build()
            val result = expressionBuilder.evaluate()
            //проверим есть есть ли точка в числе:
            val resLong = result.toLong()
            stringOfEquation = if (result == resLong.toDouble()) {
                //textViewAnsField.text = resLong.toString()
                resLong.toString()
            } else {
                //textViewAnsField.text = result.toString()
                result.toString()
            }
            //textViewEqField.text = stringOfEquation
            return stringOfEquation
        } catch (th : Throwable) {
            Toast.makeText(applicationContext, th.message, Toast.LENGTH_LONG).show()
            //textViewAnsField.text = "Bad expression!"
            return "Bad expression!"
        }
    }

    //функция проверяет, есть ли в переданной строке точка и возвращает тру, если есть
    // или строка пустая, или же если в строке еще нет чисел
    private fun haveNumberPoint(str : String): Boolean {
        //если строка пустая, то автоматически нельзя в качестве первого числа ставить точку, поэтому тру
        if (str.isEmpty()) return true
        //if we will find point, opr will be 1
        var opr = 0
        var curPosition = str.length - 1
        //если числа еще не было, тоже нельзя ставить точку перед числом, поэтому тоже тру
        if (str[curPosition] == '+' || str[curPosition] == '-' || str[curPosition] == '/' ||
            str[curPosition] == '*' || str[curPosition] == '(' || str[curPosition] == ')') {
            return true
        }
        //потом пробегаемся по строке до первого нечисла или точку. если нашли точку, то заканчиваем и говорим, что тру,
        //если добежали до нечисла и точку так и не нашли, то пишем фолс
        while (curPosition >= 0 && ((str[curPosition] in '0'..'9') || str[curPosition] == '.')) {
            if(str[curPosition] == '.') {
                opr = 1
                return true
            }
            curPosition--
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuButtonAbout -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
            R.id.menuButtonHistory -> {
                openHistoryList()
            }
            android.R.id.home -> {
                closeHistoryList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun closeHistoryList() {
        supportActionBar!!.title = getString(R.string.app_name)
        supportActionBar!!.setHomeButtonEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        //remove history
        val coordXTextViewHistory = textViewHistory.x
        val coordYTextViewHistory = textViewHistory.y
        textViewHistory.animate()
            .setDuration(300)
            .x(1000f)

        textViewHistory.postDelayed({
            textViewHistory.text = ""
        }, 300)

        textViewHistory.postDelayed({
            textViewHistory.animate()
                .setDuration(0)
                .x(coordXTextViewHistory)
        }, 300)

        scrollViewEqField.animate()
            .setDuration(300)
            .y(0f)

        scrollViewAnsField.animate()
            .setDuration(300)
            .y(350f)

        linearLayActMain.animate()
            .setDuration(300)
            .y(700f)

    }

    private fun openHistoryList() {

        textViewHistory.animate()
            .setDuration(0)
            .x(-1000f)

        history.forEach { elem ->
            textViewHistory.append("$elem \n")
        }

        textViewHistory.postDelayed({
            textViewHistory.animate()
                .setDuration(300)
                .x(0f)
        },10)



        supportActionBar!!.title = "History"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        scrollViewEqField.animate()
            .setDuration(300)
            .y(4000f)

        scrollViewAnsField.animate()
            .setDuration(300)
            .y(4000f)

        linearLayActMain.animate()
            .setDuration(300)
            .y(4000f)
    }
}
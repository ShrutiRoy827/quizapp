package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quizapp.data.Question

class QuizQuestionActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var mSelectedOption: Int = 0
    private var mUserName: String? = null
    private var mCorrectAnswer: Int = 0

    private var progressBar: ProgressBar? = null
    private var tvProgress: TextView? = null
    private var tvQuestion: TextView? = null
    private var ivImage: ImageView? = null
    private var tvOption1: TextView? = null
    private var tvOption2: TextView? = null
    private var tvOption3: TextView? = null
    private var tvOption4: TextView? = null
    private var btnSubmit: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_question)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        progressBar = findViewById(R.id.pbProgressBar)
        tvProgress = findViewById(R.id.tvProgressBar)
        tvQuestion = findViewById(R.id.tv_question)
        ivImage = findViewById(R.id.ivQuestion)
        tvOption1 = findViewById(R.id.option_1)
        tvOption2 = findViewById(R.id.option_2)
        tvOption3 = findViewById(R.id.option_3)
        tvOption4 = findViewById(R.id.option_4)
        btnSubmit = findViewById(R.id.btn_submit)

        mQuestionList = Constants.getQuestion()

        setQuestion()

        tvOption1?.setOnClickListener(this)
        tvOption2?.setOnClickListener(this)
        tvOption3?.setOnClickListener(this)
        tvOption4?.setOnClickListener(this)
        btnSubmit?.setOnClickListener(this)

    }

    private fun setQuestion() {
        val question: Question = mQuestionList!![mCurrentPosition - 1]
        defaultOptionView()
        if (mCurrentPosition == mQuestionList!!.size) {
            btnSubmit?.text = "FINISH"
        } else {
            btnSubmit?.text = "SUBMIT"
        }
        progressBar?.progress = mCurrentPosition
        tvProgress?.text = "$mCurrentPosition" + "/" + progressBar?.max
        tvQuestion?.text = question.question
        ivImage?.setImageResource(question.image)
        tvOption1?.text = question.option1
        tvOption2?.text = question.option2
        tvOption3?.text = question.option3
        tvOption4?.text = question.option4
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.option_1 -> {
                tvOption1?.let {
                    selectedOptionView(it, 1)
                }
            }
            R.id.option_2 -> {
                tvOption2?.let {
                    selectedOptionView(it, 2)
                }
            }
            R.id.option_3 -> {
                tvOption3?.let {
                    selectedOptionView(it, 3)
                }
            }
            R.id.option_4 -> {
                tvOption4?.let {
                    selectedOptionView(it, 4)
                }
            }

            R.id.btn_submit -> {
                if (mSelectedOption == 0) {
                    mCurrentPosition++

                    when{
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this@QuizQuestionActivity, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWER, mCorrectAnswer)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionList?.size)
                            startActivity(intent)
                            finish()
                        }
                    }

                } else {
                    val question = mQuestionList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOption) {
                        answerView(mSelectedOption, R.drawable.wrong_option_border)
                    } else {
                        mCorrectAnswer++
                    }

                    answerView(question.correctAnswer, R.drawable.correct_option_border)

                    if (mCurrentPosition == mQuestionList!!.size) {
                        btnSubmit?.text = "FINISH"
                    } else {
                        btnSubmit?.text = "GO TO NEXT QUESTION"
                    }
                    mSelectedOption = 0
                }
            }
        }
    }

    private fun defaultOptionView() {
        val options = ArrayList<TextView>()
        tvOption1?.let {
            options.add(0, it)
        }
        tvOption2?.let {
            options.add(1, it)
        }
        tvOption3?.let {
            options.add(2, it)
        }
        tvOption4?.let {
            options.add(3, it)
        }

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this@QuizQuestionActivity, R.drawable.default_option_border)
        }
    }

    private fun selectedOptionView(textView: TextView, selectedOption: Int) {
        defaultOptionView()
        mSelectedOption = selectedOption
        textView.setTextColor(Color.parseColor("#363A43"))
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when(answer) {
            1 -> {
                tvOption1?.background = ContextCompat.getDrawable(this@QuizQuestionActivity, drawableView)
            }
            2 -> {
                tvOption2?.background = ContextCompat.getDrawable(this@QuizQuestionActivity, drawableView)
            }
            3 -> {
                tvOption3?.background = ContextCompat.getDrawable(this@QuizQuestionActivity, drawableView)
            }
            4 -> {
                tvOption4?.background = ContextCompat.getDrawable(this@QuizQuestionActivity, drawableView)
            }
        }
    }
}
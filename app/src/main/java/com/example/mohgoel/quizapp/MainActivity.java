package com.example.mohgoel.quizapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        CheckBox.OnCheckedChangeListener, TextWatcher {
    public static final String TAG = MainActivity.class.getSimpleName();
    private Question currentQuestion;
    private PopulateQuestions mPopulateQuestions;
    private ArrayList<Question> mQuestionaire;
    private String clickedAnswer;

    //Views Declaration
    private TextView tvQuestionStatement;
    private RadioGroup radioGroupChoices;
    private SelectableRoundedImageView mImageView;
    private LinearLayout llRootView, llOptionsView;
    private CheckBox cbOption1, cbOption2, cbOption3, cbOption4;
    private EditText etTextualChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (SelectableRoundedImageView) findViewById(R.id.question_image_view);
        llRootView = (LinearLayout) findViewById(R.id.rootview_linear_layout);
        llOptionsView = (LinearLayout) findViewById(R.id.options_view_linear_layout);
        tvQuestionStatement = (TextView) findViewById(R.id.question_stmt_text_view);
        cbOption1 = (CheckBox) findViewById(R.id.checkbox_option_1);
        cbOption2 = (CheckBox) findViewById(R.id.checkbox_option_2);
        cbOption3 = (CheckBox) findViewById(R.id.checkbox_option_3);
        cbOption4 = (CheckBox) findViewById(R.id.checkbox_option_4);
        etTextualChoice = (EditText) findViewById(R.id.textual_choice_edit_text_view);

        initQue();
    }

    private void initQue() {
        // Create Questionaire
        mPopulateQuestions = new PopulateQuestions(this);
        mQuestionaire = mPopulateQuestions.createQuestionaire();
        Log.d(TAG, mQuestionaire.toString());

        initUI();
    }

    private void initUI() {
        currentQuestion = mQuestionaire.get(0);
        updateThumbnailImage();

        if (currentQuestion.getType() == Question.QUESTION_TYPE.SINGLE) {
            updateUIForSingleChoiceQuestion(currentQuestion);
        } else if (currentQuestion.getType() == Question.QUESTION_TYPE.TEXT) {
            updateUIForTextQuestion(currentQuestion);
        } else if (currentQuestion.getType() == Question.QUESTION_TYPE.MULTIPLE) {
            updateUIForMultipleChoiceQuestion(currentQuestion);
        }
    }


    public void handleAnswer(View view) {
        if (view instanceof Button) {
            String text = ((Button) view).getText().toString().substring(1);
            int clickedQuestionId = Integer.parseInt(text);
            int currentQuestionId = currentQuestion.getId();

            if (clickedQuestionId != currentQuestionId) {
                if (!currentQuestion.isAttempted() || clickedAnswer == null) {
                    //Move to targetQuestion
                    moveToQuestionById(clickedQuestionId);
                } else {
                    //Display Custom Pop Up Dialog with Save Yes or no
                    displayDialog(clickedQuestionId, currentQuestionId);
                }
            }
        }
    }


    private void moveToQuestionById(int id) {
        currentQuestion = mPopulateQuestions.getQuestionById(id);
        updateUIForQuestion(currentQuestion);
    }

    private void updateUIForQuestion(Question question) {
        updateThumbnailImage();
        if (question.getType() == Question.QUESTION_TYPE.SINGLE) {
            updateUIForSingleChoiceQuestion(question);
        } else if (question.getType() == Question.QUESTION_TYPE.TEXT) {
            updateUIForTextQuestion(question);
        } else if (question.getType() == Question.QUESTION_TYPE.MULTIPLE) {
            updateUIForMultipleChoiceQuestion(question);
        }
    }


    private void updateUIForSingleChoiceQuestion(Question question) {
        resetClickedAnswer();
        tvQuestionStatement.setText(question.getStatement());
        llOptionsView.removeAllViews();
        radioGroupChoices = new RadioGroup(this);
        radioGroupChoices.setOnCheckedChangeListener(this);
        llOptionsView.addView(radioGroupChoices);
        cbOption1.setVisibility(View.GONE);
        cbOption2.setVisibility(View.GONE);
        cbOption3.setVisibility(View.GONE);
        cbOption4.setVisibility(View.GONE);
        etTextualChoice.setVisibility(View.GONE);

        for (int i = 0; i < 4; i++) {
            String s = question.getOptions()[i];
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(s);
            radioButton.setId(i);
            RadioGroup.LayoutParams params = new RadioGroup
                    .LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioGroupChoices.addView(radioButton, params);
            String ans = question.getUserAnswer();
            if (ans != null && !ans.isEmpty() && ans.equals(s)) {
                radioButton.setChecked(true);
                resetClickedAnswer();
            }
        }
    }

    private void updateUIForTextQuestion(Question question) {
        resetClickedAnswer();
        tvQuestionStatement.setText(question.getStatement());
        llOptionsView.removeAllViews();
        etTextualChoice.setVisibility(View.VISIBLE);
        etTextualChoice.addTextChangedListener(this);
        llOptionsView.addView(etTextualChoice);

        String ans = question.getUserAnswer();
        if (ans != null && !ans.isEmpty()) {
            etTextualChoice.setText(ans);
            resetClickedAnswer();
        }
    }

    private void updateUIForMultipleChoiceQuestion(Question question) {
        resetClickedAnswer();
        tvQuestionStatement.setText(question.getStatement());

        llOptionsView.removeAllViews();
        cbOption1.setVisibility(View.VISIBLE);
        cbOption2.setVisibility(View.VISIBLE);
        cbOption3.setVisibility(View.VISIBLE);
        cbOption4.setVisibility(View.VISIBLE);

        llOptionsView.addView(cbOption1);
        llOptionsView.addView(cbOption2);
        llOptionsView.addView(cbOption3);
        llOptionsView.addView(cbOption4);

        cbOption1.setChecked(false);
        cbOption2.setChecked(false);
        cbOption3.setChecked(false);
        cbOption4.setChecked(false);

        cbOption1.setText(question.getOptions()[0]);
        cbOption2.setText(question.getOptions()[1]);
        cbOption3.setText(question.getOptions()[2]);
        cbOption4.setText(question.getOptions()[3]);

        cbOption1.setOnCheckedChangeListener(this);
        cbOption2.setOnCheckedChangeListener(this);
        cbOption3.setOnCheckedChangeListener(this);
        cbOption4.setOnCheckedChangeListener(this);

        String ans = question.getUserAnswer();
        if (ans != null && !ans.isEmpty()) {
            String s = cbOption1.getText().toString();
            if (ans.contains(s)) {
                cbOption1.setChecked(true);
            }

            s = cbOption2.getText().toString();
            if (ans.contains(s)) {
                cbOption2.setChecked(true);
            }

            s = cbOption3.getText().toString();
            if (ans.contains(s)) {
                cbOption3.setChecked(true);
            }

            s = cbOption4.getText().toString();
            if (ans.contains(s)) {
                cbOption4.setChecked(true);
            }
            resetClickedAnswer();
        }
    }

    private void updateThumbnailImage() {
        mImageView.setImageResource(currentQuestion.getImageId());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bmpImage = BitmapFactory.decodeResource(getResources(), currentQuestion.getImageId(), options);
        Bitmap newImage = Blur.fastblur(MainActivity.this, bmpImage, 12);
        BitmapDrawable background = new BitmapDrawable(newImage);
        llRootView.setBackgroundDrawable(background);
    }

    private void displayDialog(final int clickedQuestionId, final int currentQuestionId) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_yesno);

        TextView tvMessage = (TextView) dialog.findViewById(R.id.textDialogYesNoMessage);
        tvMessage.setText("Do You want to save your answer ?");

        Button btnYes = (Button) dialog.findViewById(R.id.btnDialogYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //do save the answer
                saveAnswer(clickedQuestionId);
            }
        });

        Button btnNo = (Button) dialog.findViewById(R.id.btnDialogNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToQuestionById(clickedQuestionId);
                dialog.dismiss();
                currentQuestion.setAttempted(false);
            }
        });

        dialog.show();
    }

    private void saveAnswer(int clickedQuestionId) {
        currentQuestion.setUserAnswer(clickedAnswer);
        moveToQuestionById(clickedQuestionId);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId != -1) {
            RadioButton radioButton = findViewById(checkedId);
            clickedAnswer = radioButton.getText().toString();
            currentQuestion.setAttempted(true);
        }
    }

    private void resetClickedAnswer() {
        clickedAnswer = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        StringBuilder stringBuilder = new StringBuilder();
        if (cbOption1.isChecked()) {
            stringBuilder.append("-").append(cbOption1.getText());
        }
        if (cbOption2.isChecked()) {
            stringBuilder.append("-").append(cbOption2.getText());
        }
        if (cbOption3.isChecked()) {
            stringBuilder.append("-").append(cbOption3.getText());
        }
        if (cbOption4.isChecked()) {
            stringBuilder.append("-").append(cbOption4.getText());
        }

        String oldValue = clickedAnswer;
        clickedAnswer = stringBuilder.toString();
        if (!clickedAnswer.isEmpty()) {
            currentQuestion.setAttempted(true);
        } else if (oldValue != null && !oldValue.isEmpty() && clickedAnswer.isEmpty()) {
            currentQuestion.setAttempted(true);
        } else {
            currentQuestion.setAttempted(false);

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        clickedAnswer = s.toString();
        if (clickedAnswer != null && !clickedAnswer.isEmpty()) {
            currentQuestion.setAttempted(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void reset(View view) {
        resetClickedAnswer();
        currentQuestion.setUserAnswer(clickedAnswer);
        updateUIForQuestion(currentQuestion);
    }

    public void nextQue(View view) {
        if (currentQuestion != null) {
            saveAnswer(currentQuestion.getId());
        }
        if (mPopulateQuestions.getIsAllAttempted()) {
            findViewById(R.id.button1).setVisibility(View.GONE);
            findViewById(R.id.button).setVisibility(View.GONE);
            findViewById(R.id.button3).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.button3).setVisibility(View.GONE);
        }
        if (currentQuestion.getId() < 10)
            moveToQuestionById(currentQuestion.getId() + 1);
        else if (!mPopulateQuestions.getIsAllAttempted())
            moveToQuestionById(1);
    }

    public void submit(View view) {
        Toast.makeText(this, "Your score is " + mPopulateQuestions.getScore(), Toast.LENGTH_LONG).show();
        displayResultDialog(mPopulateQuestions.getScore());
        initQue();
        findViewById(R.id.button1).setVisibility(View.VISIBLE);
        findViewById(R.id.button).setVisibility(View.VISIBLE);
        findViewById(R.id.button3).setVisibility(View.GONE);
    }

    private void displayResultDialog(int score) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_result);

        TextView tvCorrectCount = (TextView) dialog.findViewById(R.id.correct_count_textview);
        TextView tvInCorrectCount = (TextView) dialog.findViewById(R.id.incorrect_count_textview);
        tvCorrectCount.setText(score+"");
        tvInCorrectCount.setText((10-score) + "");



        dialog.show();
    }
}

package com.example.llm_project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    List<QuizQuestion> questions;

    public HistoryAdapter(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: inflate item_history_row.xml and return a new HistoryViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        QuizQuestion q = questions.get(position);

        // TODO: set tvQuestion text
        TextView tvQuestion = holder.tvQuestion;
        tvQuestion.setText(q.question);
        // TODO: set tvYourAnswer text
        TextView tvYourAnswer = holder.tvYourAnswer;
        tvYourAnswer.setText("Your answer: " + q.answer);
        // TODO: set tvCorrectAnswer text
        TextView tvCorrectAnswer = holder.tvCorrectAnswer;
        tvCorrectAnswer.setText("Correct answer: " + q.correctAnswer);
        // TODO: if q.answer equals q.correctAnswer, show ✓ in green, otherwise ✗ in red
        TextView tvResult = holder.tvResult;
        if (q.answer.equals(q.correctAnswer)) {
            holder.tvResult.setText("✓");
            holder.tvResult.setTextColor(Color.parseColor("#60D394"));
        } else {
            holder.tvResult.setText("✗");
            holder.tvResult.setTextColor(Color.parseColor("#EF4444"));
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvYourAnswer, tvCorrectAnswer, tvResult;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            // TODO: findViewById for all four TextViews
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvYourAnswer = itemView.findViewById(R.id.tvYourAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvResult = itemView.findViewById(R.id.tvResult);
        }
    }
}
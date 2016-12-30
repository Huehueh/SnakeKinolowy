package com.example.user.snake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.snake.communication.Answers.Score;

/**
 * Created by user on 29.12.2016.
 */
public class ResultsAdapter extends ArrayAdapter<Score>{

    Context context;
    Score[] scores;
    public ResultsAdapter(Context context, Score[] scores) {
        super(context, R.layout.results_row, scores);
        this.context = context;
        this.scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.results_row, parent, false);
        TextView name = (TextView)rowView.findViewById(R.id.snakeName);
        TextView points = (TextView)rowView.findViewById(R.id.snakePoints);
        TextView deaths = (TextView)rowView.findViewById(R.id.snakeDeaths);
        TextView hits = (TextView)rowView.findViewById(R.id.snakeHits);

        Score score = scores[position];
        name.setText(score.getName());
        points.setText(score.getPoints()+"");
        deaths.setText(score.getDeaths()+"");
        hits.setText(score.getHits()+"");

        return rowView;
    }
}

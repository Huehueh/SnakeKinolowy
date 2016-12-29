package com.example.user.snake;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.snake.communication.Answers.ScoreMessage;
import com.example.user.snake.main.GameFragment;

/**
 * Created by user on 29.12.2016.
 */
public class ResultsDialogFragment extends DialogFragment implements View.OnClickListener{

    ScoreMessage scoreMessage;
    static String scoreKey = "score";
    GameFragment gameFragment;

    public static ResultsDialogFragment newInstance(ScoreMessage message, GameFragment gameFragment)
    {
        ResultsDialogFragment fragment = new ResultsDialogFragment();
        fragment.gameFragment = gameFragment;
        Bundle arguments = new Bundle();
        arguments.putSerializable(scoreKey, message);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scoreMessage = (ScoreMessage) getArguments().getSerializable(scoreKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.results_dialog, container, false);

        View header = inflater.inflate(R.layout.results_row, null);
        ResultsAdapter adapter = new ResultsAdapter(getActivity(), scoreMessage.getScores());
        ListView dialogListView = (ListView)rootView.findViewById(R.id.dialogListView);
        setHeader(header, dialogListView);
        dialogListView.setAdapter(adapter);

        rootView.findViewById(R.id.cancelDialogButton).setOnClickListener(this);

        return rootView;
    }

    private void setHeader(View header, ListView listView)
    {
        TextView name = (TextView)header.findViewById(R.id.snakeName);
        name.setText(getText(R.string.name));
        TextView points = (TextView)header.findViewById(R.id.snakePoints);
        points.setText(getText(R.string.points));
        TextView deaths = (TextView)header.findViewById(R.id.snakeDeaths);
        deaths.setText(getText(R.string.deaths));
        listView.addHeaderView(header);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cancelDialogButton:
                this.dismiss();
                gameFragment.onExitDialog();
                break;

        }
    }
}

package com.ghofrani.classapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ghofrani.classapp.R;
import com.woalk.apps.lib.colorpicker.ColorPickerDialog;
import com.woalk.apps.lib.colorpicker.ColorPickerSwatch;

public class AddClassActivity extends AppCompatActivity {

    int selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        Toolbar addClassToolbar = (Toolbar) findViewById(R.id.add_class_toolbar);
        addClassToolbar.setTitle("Add Class");
        addClassToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(addClassToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedColor = Color.BLACK;

        Button colorPickerButton = (Button) findViewById(R.id.color_picker_button);
        colorPickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ColorPickerDialog dialog = ColorPickerDialog.newInstance(
                        "Select a color",
                        new int[] {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.LTGRAY, Color.MAGENTA, Color.WHITE, Color.YELLOW},
                        selectedColor,
                        4,
                        ColorPickerDialog.SIZE_SMALL);

                dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener(){

                    @Override
                    public void onColorSelected(int color) {
                        selectedColor = color;
                    }

                });

                dialog.show(getFragmentManager(), "color_picker");

            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Discard changes?");

            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();

                    callSuperOnBackPressed();

                }

            });

            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();

                }

            });

            builder.create().show();

            return true;

        } else {

            return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Discard changes?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();

                callSuperOnBackPressed();

            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();

            }

        });

        builder.create().show();

    }

    private void callSuperOnBackPressed(){

        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_check, menu);

        return true;

    }

    @Override
    protected void onStart() {

        super.onStart();

    }

}
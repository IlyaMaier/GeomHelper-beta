package com.example.geomhelper.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.geomhelper.R;
import com.example.geomhelper.content.Course;
import com.example.geomhelper.content.Courses;
import com.example.geomhelper.content.Theorems;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.geomhelper.activities.MainActivity.back;

public class FragmentTheorems extends Fragment {

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private List<String> themes;
    private List<String> theorems;
    private List<String> themes1;
    private List<String> theorems1;
    private List<Integer> images, images1;
    private CardView card;
    private Theorems theoremsClass;
    private int height;
    private float y;
    private long millis;
    private int f = 7, t = 1;
    private boolean search = false, form = true, theme = false, def = false, s = false;
    public static volatile boolean h = true;

    public FragmentTheorems() {
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theorems, container, false);

        getThread().start();

        theoremsClass = new Theorems();

        themes = theoremsClass.getThemes();
        theorems = theoremsClass.getTheorems();
        images = theoremsClass.getImages();
        themes1 = new ArrayList<>();
        theorems1 = new ArrayList<>();
        images1 = new ArrayList<>();
        List<String> forms = new ArrayList<>();
        List<String> thems = new ArrayList<>();

        List<Course> courses = new Courses().getCurrentCourses();
        for (int i = 0; i < courses.size(); i++)
            thems.add(courses.get(i).getCourseName());

        forms.add("7 класс");

        rvAdapter = new RVAdapter();
        rvAdapter.setForms(forms);
        rvAdapter.setThemes(thems);

        recyclerView = view.findViewById(R.id.rv_the);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rvAdapter);

        FrameLayout frameLayout = view.findViewById(R.id.frame_the);

        card = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.card_search, null);
        int width = 1000;
        height = 175;
        WindowManager wm = (WindowManager) Objects.requireNonNull(
                getContext()).getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            width = wm.getDefaultDisplay().getWidth() - 50;
            height = (int) (wm.getDefaultDisplay().getHeight() * 0.1);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        frameLayout.addView(card, layoutParams);
        card.setTranslationY(-height);

        EditText et = view.findViewById(R.id.search);
        et.setHint(R.string.search_theorems);

        final int finalHeight = height;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (System.currentTimeMillis() - millis > 500) {
                    y = event.getY();
                    millis = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (event.getY() - y > 180) {
                        recyclerView.animate().translationY(finalHeight + 25).start();
                        card.animate().translationY(25).alpha(1).start();
                        s = true;
                        back = 8;
                    } else if (y - event.getY() > 50) {
                        card.animate().translationY(-height).alpha(0).start();
                        recyclerView.animate().translationY(0).start();
                        s = false;
                    }
                }
                return false;
            }

        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    search = false;
                    rvAdapter.notifyDataSetChanged();
                } else {
                    themes1 = new ArrayList<>();
                    theorems1 = new ArrayList<>();
                    images1 = new ArrayList<>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < themes.size(); i++) {
                                if (themes.get(i).toLowerCase().contains(s.toString().toLowerCase())) {
                                    themes1.add(themes.get(i));
                                    theorems1.add(theorems.get(i));
                                    images1.add(images.get(i));
                                }
                            }
                            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    search = true;
                                    rvAdapter.setDefsSearch(themes1);
                                    rvAdapter.setDefinitionsSearch(theorems1);
                                    rvAdapter.setImagesSearch(images1);
                                    rvAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }).start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        BottomNavigationView bottomNavigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        return view;
    }

    class RVAdapter extends RecyclerView.Adapter<RVAdapter.DefViewHolder> {

        private List<String> defs;
        private List<String> definitions;
        private List<String> defsSearch;
        private List<String> definitionsSearch;
        private List<Integer> images;
        private List<Integer> imagesSearch;
        private List<String> forms;
        private List<String> themes;

        RVAdapter() {
        }

        @NonNull
        @Override
        public DefViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_the,
                    parent, false);
            return new DefViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DefViewHolder holder, int position) {
            holder.id = holder.getAdapterPosition();
            String d, d1 = "";
            int img = 0;
            if (search) {
                d = defsSearch.get(position);
                d1 = " - " + definitionsSearch.get(position);
                img = imagesSearch.get(position);
            } else if (form)
                d = forms.get(position);
            else if (theme)
                d = themes.get(position);
            else {
                d = defs.get(position);
                d1 = definitions.get(position);
                img = images.get(position);
            }
            holder.text1.setText(d);
            holder.text2.setText(d1);
            holder.img.setBackgroundResource(img);
        }

        @Override
        public int getItemCount() {
            if (search)
                return defsSearch.size();
            if (form)
                return forms.size();
            if (theme)
                return themes.size();
            if (def)
                return defs.size();
            return 0;
        }

        public void setDefs(List<String> defs) {
            this.defs = defs;
        }

        public void setDefinitions(List<String> definitions) {
            this.definitions = definitions;
        }

        void setForms(List<String> forms) {
            this.forms = forms;
        }

        void setThemes(List<String> themes) {
            this.themes = themes;
        }

        public void setDefsSearch(List<String> defsSearch) {
            this.defsSearch = defsSearch;
        }

        public void setImages(List<Integer> images) {
            this.images = images;
        }

        public void setImagesSearch(List<Integer> imagesSearch) {
            this.imagesSearch = imagesSearch;
        }

        public void setDefinitionsSearch(List<String> definitionsSearch) {
            this.definitionsSearch = definitionsSearch;
        }

        class DefViewHolder extends RecyclerView.ViewHolder {

            TextView text1, text2;
            ImageView img;
            int id;

            DefViewHolder(final View itemView) {
                super(itemView);

                text1 = itemView.findViewById(R.id.text_the1);
                text2 = itemView.findViewById(R.id.text_the2);
                img = itemView.findViewById(R.id.image_the);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (form) {
                            f = id + 7;
                            form = false;
                            theme = true;
                            rvAdapter.notifyDataSetChanged();
                            back = 8;
                        } else if (theme) {
                            t = id + 1;
                            theme = false;
                            def = true;
                            rvAdapter.notifyDataSetChanged();
                            defs = theoremsClass.getThemes(f, t);
                            definitions = theoremsClass.getTheorems(f, t);
                            images = theoremsClass.getImages(f, t);
                            rvAdapter.notifyDataSetChanged();
                            back = 8;
                        }
                    }
                });
            }

        }

    }

    Thread getThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (h) {
                }
                try {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (s) {
                                card.animate().translationY(-height).alpha(0).start();
                                recyclerView.animate().translationY(0).start();
                                s = false;
                                if (form)
                                    back = 6;
                            } else if (theme) {
                                theme = false;
                                form = true;
                                rvAdapter.notifyDataSetChanged();
                                back = 6;
                            } else if (def) {
                                def = false;
                                theme = true;
                                rvAdapter.notifyDataSetChanged();
                            }
                            h = true;
                            getThread().start();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

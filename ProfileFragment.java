package j.edu.wasp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    LinearLayout familyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        familyList = (LinearLayout) view.findViewById(R.id.familyListLayout);
        prepareFamily();
        return view;

    }

    public void prepareFamily() {


            ArrayList<String> listData = new ArrayList<>();

            for(FamilyMember fam: MapsActivity.family.getFamily()) {
                final FamilyMember accessFam = fam;
                TextView member = new TextView(getContext());
                member.setText(fam.getFName() + " " + fam.getlName());
                member.setPadding(0, 10, 0, 10);
                member.setTextSize(20);
                member.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), EditFam.class);
                        intent.putExtra("id", accessFam.getId());
                        startActivity(intent);
                    }
                });

                familyList.addView(member);

            }

    }
}

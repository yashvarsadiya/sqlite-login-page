package com.example.sqlitedatabasedemo1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static final String TAG = MyAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<UserModal> userModal;
    OnClick onClick;

    public MyAdapter(Context context, ArrayList<UserModal> userModal, OnClick onClick) {
        this.context = context;
        this.userModal = userModal;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name_id.setText(String.valueOf(userModal.get(position).getName()));
        holder.email_id.setText(String.valueOf(userModal.get(position).getEmail()));
        holder.gender_id.setText(String.valueOf(userModal.get(position).getGender()));
        holder.image_id.setImageURI(Uri.parse(String.valueOf(userModal.get(position).getImage())));
        Log.e(TAG, "HOLDER NAME SHOW " + userModal.get(position).getName());
        Log.e(TAG, "HOLDER EMAIL SHOW " + userModal.get(position).getEmail());
        Log.e(TAG, "HOLDER Gender SHOW " + userModal.get(position).getGender());
        Log.e(TAG, "HOLDER Image SHOW " + userModal.get(position).getImage());

        holder.user_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "CLICKED ");
                String email1 = holder.email_id.getText().toString();

                Intent intent = new Intent(context, Login.class);   // aa intent login screen ma jashe
                intent.putExtra("email", userModal.get(position).getEmail());
                context.startActivity(intent);
                Log.e(TAG, "My Adapter to Login Page " + email1);

//                Intent intent = new Intent(context, Profile.class);  // aa intent profile.class na page ma jashe
//                intent.putExtra("key_email", email1);
//                context.startActivity(intent);
//                Log.e(TAG,"profile page open "+ email1);

                onClick.onClick(userModal.get(position).getName(), userModal.get(position).getEmail(), userModal.get(position).getGender(), userModal.get(position).getImage());

            }
        });

        holder.three_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.three_dot);
                popupMenu.getMenuInflater().inflate(R.menu.option_menubars, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        int id = menuItem.getItemId();
                        if (id == R.id.option_1) {

                            Intent intent = new Intent(context, UpdateProfile.class);
                            intent.putExtra("key_usermodal", userModal.get(position));
                            context.startActivity(intent);
                            Log.e(TAG, "My Adapter to Update Profile " + userModal.get(position));
                            Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show();

                        } else if (id == R.id.option_2) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Delete Profile");
                            builder.setMessage("Are You Sure You Want To Delete Your Profile ?");

                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DbHelper dbHelper = new DbHelper(context);
                                    boolean b = dbHelper.deleteProfileHelper(userModal.get(position).getEmail());
                                    if (b) {
                                        Toast.makeText(context, "Profile Delete Successfully..", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, Userlist.class));
                                    } else {
                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            builder.show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "SIZE SHOW " + userModal.size());
        return userModal.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_id, email_id, gender_id;
        CircleImageView image_id;
        LinearLayout user_cardView;
        ImageView three_dot;

        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_id = itemView.findViewById(R.id.textname);
            email_id = itemView.findViewById(R.id.textemail);
            gender_id = itemView.findViewById(R.id.textgender);
            image_id = itemView.findViewById(R.id.userentry_image);
            user_cardView = itemView.findViewById(R.id.user_cardView);
            three_dot = itemView.findViewById(R.id.three_dot);
            Log.e(TAG, "MyViewHolder WORKED ");

        }
    }

    public interface OnClick {
        void onClick(String name, String email, String gender, String image);
    }
}
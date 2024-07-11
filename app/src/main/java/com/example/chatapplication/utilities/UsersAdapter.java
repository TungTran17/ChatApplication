package com.example.chatapplication.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.models.User;

import java.util.List;

//class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

//    private final List<User> users;
//    private final UserDeleteListener deleteListener;
//
//    public UsersAdapter(List<User> users, UserDeleteListener deleteListener) {
//        this.users = users;
//        this.deleteListener = deleteListener;
//    }
//
//    @NonNull
//    @Override
//    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false);
//        return new UserViewHolder(view, deleteListener);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
//        holder.bind(users.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return users.size();
//    }
//
//    static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView textName;
//        private final TextView textEmail;
//        private final ImageView imageProfile;
//        private final Button buttonDelete;
//        private final UserDeleteListener deleteListener;
//
//        public UserViewHolder(@NonNull View itemView, UserDeleteListener deleteListener) {
//            super(itemView);
//            textName = itemView.findViewById(R.id.textName);
//            textEmail = itemView.findViewById(R.id.textEmail);
//            imageProfile = itemView.findViewById(R.id.imageProfile);
//            buttonDelete = itemView.findViewById(R.id.buttonDelete);
//            this.deleteListener = deleteListener;
//
//            buttonDelete.setOnClickListener(v -> deleteListener.onUserDelete(getAdapterPosition()));
//        }
//
//        void bind(User user) {
//            textName.setText(user.name);
//            textEmail.setText(user.email);
//            if (user.image != null && !user.image.isEmpty()) {
//                byte[] bytes = Base64.decode(user.image, Base64.DEFAULT);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                imageProfile.setImageBitmap(bitmap);
//            }
//        }
//    }
//
//    public interface UserDeleteListener {
//        void onUserDelete(int position);
//    }
//}

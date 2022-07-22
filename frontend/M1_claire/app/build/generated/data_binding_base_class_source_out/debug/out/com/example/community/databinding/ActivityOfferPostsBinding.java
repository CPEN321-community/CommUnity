// Generated by view binder compiler. Do not edit!
package com.example.community.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.community.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityOfferPostsBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final FloatingActionButton addOfferPostButton;

  @NonNull
  public final ImageButton searchOfferPostButton;

  private ActivityOfferPostsBinding(@NonNull ConstraintLayout rootView,
      @NonNull FloatingActionButton addOfferPostButton,
      @NonNull ImageButton searchOfferPostButton) {
    this.rootView = rootView;
    this.addOfferPostButton = addOfferPostButton;
    this.searchOfferPostButton = searchOfferPostButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityOfferPostsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityOfferPostsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_offer_posts, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityOfferPostsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.addOfferPostButton;
      FloatingActionButton addOfferPostButton = ViewBindings.findChildViewById(rootView, id);
      if (addOfferPostButton == null) {
        break missingId;
      }

      id = R.id.searchOfferPostButton;
      ImageButton searchOfferPostButton = ViewBindings.findChildViewById(rootView, id);
      if (searchOfferPostButton == null) {
        break missingId;
      }

      return new ActivityOfferPostsBinding((ConstraintLayout) rootView, addOfferPostButton,
          searchOfferPostButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
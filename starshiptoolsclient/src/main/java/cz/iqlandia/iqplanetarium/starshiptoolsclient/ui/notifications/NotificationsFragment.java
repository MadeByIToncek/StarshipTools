/*
 * Copyright (c) 2023 - IToncek
 *
 * All rights to modifying this source code are granted, except for changing licence.
 * Any and all products generated from this source code must be shared with a link
 * to the original creator with clear and well-defined mention of the original creator.
 * This applies to any lower level copies, that are doing approximately the same thing.
 * If you are not sure, if your usage is within these boundaries, please contact the
 * author on their public email address.
 */

package cz.iqlandia.iqplanetarium.starshiptoolsclient.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import cz.iqlandia.iqplanetarium.starshiptoolsclient.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {
	
	private FragmentNotificationsBinding binding;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		NotificationsViewModel notificationsViewModel =
				new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NotificationsViewModel.class);
		
		binding = FragmentNotificationsBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		
		final TextView textView = binding.textNotifications;
		notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		return root;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
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

package cz.iqlandia.iqplanetarium.starshiptoolsclient.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProvider;
import cz.iqlandia.iqplanetarium.starshiptoolsclient.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
	
	private FragmentDashboardBinding binding;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		DashboardViewModel dashboardViewModel =
				new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);
		
		binding = FragmentDashboardBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		
		final TextView textView = binding.textDashboard;
		dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		return root;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}
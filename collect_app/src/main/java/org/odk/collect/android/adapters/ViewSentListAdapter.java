/*
 * Copyright 2017 SDRC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.odk.collect.android.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.odk.collect.android.R;
import org.odk.collect.android.provider.FormsProviderAPI;
import org.odk.collect.android.provider.InstanceProviderAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewSentListAdapter extends SimpleCursorAdapter {
    private Context mContext;

    public ViewSentListAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        Long date = getCursor().getLong(getCursor().getColumnIndex(InstanceProviderAPI.InstanceColumns.DELETED_DATE));

        String selection = FormsProviderAPI.FormsColumns.JR_FORM_ID + " =? ";
        String[] selectionArgs = {getCursor().getString(getCursor().getColumnIndex(InstanceProviderAPI.InstanceColumns.JR_FORM_ID))};

        //getting blank form record
        Cursor cursor = mContext.getContentResolver().query(FormsProviderAPI.FormsColumns.CONTENT_URI, null,
                selection, selectionArgs, null);

        boolean formExists = false;
        boolean isFormEncrypted = false;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int base64RSAPublicKeyColumnIndex = cursor.getColumnIndex(FormsProviderAPI.FormsColumns.BASE64_RSA_PUBLIC_KEY);
                    String base64RSAPublicKey = cursor.getString(base64RSAPublicKeyColumnIndex);
                    isFormEncrypted = base64RSAPublicKey != null && !base64RSAPublicKey.isEmpty();
                    formExists = true;
                }
            } finally {
                cursor.close();
            }
        }

        TextView visibilityOffCause = (TextView) view.findViewById(R.id.text4);
        ImageView visibleOff = (ImageView) view.findViewById(R.id.visible_off);
        if (date != 0 || !formExists || isFormEncrypted) {
            visibilityOffCause.setVisibility(View.VISIBLE);
            visibleOff.setVisibility(View.VISIBLE);

            if (date != 0) {
                visibilityOffCause.setText(
                        new SimpleDateFormat(mContext.getString(R.string.deleted_on_date_at_time),
                                Locale.getDefault()).format(new Date(date)));
            } else if (!formExists) {
                visibilityOffCause.setText(mContext.getString(R.string.deleted_form));
            } else {
                visibilityOffCause.setText(mContext.getString(R.string.encrypted_form));
            }
        } else {
            visibilityOffCause.setVisibility(View.GONE);
            visibleOff.setVisibility(View.GONE);
        }
        return view;
    }
}
package com.practice.andr_networking_asm.ui.product_manager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.practice.andr_networking_asm.DAO.DAO;
import com.practice.andr_networking_asm.R;
import com.practice.andr_networking_asm.adapter.ProductAdapter;
import com.practice.andr_networking_asm.Callback.OnListenerRecyclerViewItems;
import com.practice.andr_networking_asm.Callback.VolleyCallback;
import com.practice.andr_networking_asm.model.mProducts;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementFragment extends Fragment{
    String TAG = "Product Fragment";
    private EditText edtProductName, edtProductCat, edtProductPrice;
    private Button btnInsert, btnCancel;
    private FloatingActionButton fab;
    DAO DAO;
    mProducts mProducts;
    private static ProductAdapter adapter;
    private static List<mProducts> productsList = new ArrayList<>();
    RecyclerView rcv;
    View root;

    VolleyCallback volleyCallback;
    OnListenerRecyclerViewItems onListenerRecyclerViewItems;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_new_content, container, false);

        initViews();

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertProductDialog();
            }
        });

        openUpdateProductFragmentAndDeleteProduct();

        return root;
    }

    private void initViews(){

        rcv = root.findViewById(R.id.rcv_products);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

        volleyCallback = new VolleyCallback() {
            @Override
            public void onSuccessResponseGetAll(List<mProducts> list) {
                productsList.clear();
                productsList.addAll(list);
                adapter = new ProductAdapter(productsList, getActivity(), onListenerRecyclerViewItems);
                rcv.setAdapter(adapter);
                rcv.scheduleLayoutAnimation();
            }

            @Override
            public void onSuccessResponse(String result, mProducts mProducts) {
                if(Integer.parseInt(result) == 1){
                    Toast.makeText(getActivity(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    productsList.add(productsList.size(), mProducts);
                    adapter.notifyDataSetChanged();
                }else {
                    Log.d(TAG, "onSuccessResponseInsert: " + result);
                }
            }

            @Override
            public void onErrorResponse(String result) {
            }
        };

        DAO = new DAO(getActivity());
        DAO.getAllProduct(volleyCallback);

    }

    private void insertProductDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.inser_product_dialog);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_login));

        edtProductName = dialog.findViewById(R.id.edtProductName);
        edtProductCat = dialog.findViewById(R.id.edtProductCat);
        edtProductPrice = dialog.findViewById(R.id.edtProductPrice);
        btnInsert = dialog.findViewById(R.id.btnInsertProduct);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        DAO = new DAO(getActivity());
        mProducts = new mProducts();

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProducts.setProductName(edtProductName.getText().toString());
                mProducts.setProductCat(edtProductCat.getText().toString());
                mProducts.setProductPrice(Double.parseDouble(edtProductPrice.getText().toString()));
                DAO.insertProduct(mProducts, volleyCallback);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void openUpdateProductFragmentAndDeleteProduct(){
        onListenerRecyclerViewItems = new OnListenerRecyclerViewItems() {
            @Override
            public void onItemRecyclerViewClick(mProducts mProducts, int position) {
                EditProductFragment editProductFragment = EditProductFragment.newInstance();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit, R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
                transaction.add(R.id.nav_host_fragment, editProductFragment);
                transaction.commit();

                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                bundle.putInt("id", mProducts.getId());
                bundle.putString("name", mProducts.getProductName());
                bundle.putString("cat", mProducts.getProductCat());
                bundle.putDouble("price", mProducts.getProductPrice());
                editProductFragment.setArguments(bundle);
            }

            @Override
            public void onItemDelete(final mProducts m, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.dialog_fire_missiles)
                        .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                volleyCallback = new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponseGetAll(List<com.practice.andr_networking_asm.model.mProducts> list) {

                                    }

                                    @Override
                                    public void onSuccessResponse(String result, com.practice.andr_networking_asm.model.mProducts mProducts) {
                                        if (Integer.parseInt(result) == 1) {
                                            Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                            productsList.remove(position);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(getActivity(), "Xóa toang rồi!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onErrorResponse(String result) {

                                    }
                                };

                                DAO = new DAO(getActivity());
                                DAO.deleteProduct(volleyCallback, m);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        };
    }

    public static void updateProductList(mProducts m, int pos){
        if(m == null){
            return;
        }
        productsList.remove(pos);
        productsList.add(pos, m);
        adapter.notifyDataSetChanged();
    }

}
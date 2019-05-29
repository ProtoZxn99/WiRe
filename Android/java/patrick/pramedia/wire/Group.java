package patrick.pramedia.wire;


import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import patrick.pramedia.wire.adapter.AdapterChooseDevice;
import patrick.pramedia.wire.adapter.AdapterKelompok;
import patrick.pramedia.wire.adapter.AdapterKelompokS;
import patrick.pramedia.wire.adapter.AdapterRemoveDevice;
import patrick.pramedia.wire.adapter.interfaceRVKelompok;
import patrick.pramedia.wire.entitas.EntitasDevice;
import patrick.pramedia.wire.entitas.Kelompok;
import patrick.pramedia.wire.entitas.RemoveDevice;
import patrick.pramedia.wire.modul.Globals;
import patrick.pramedia.wire.modul.SecurityAES128CBC;
import patrick.pramedia.wire.modul.SecuritySHA256;
import patrick.pramedia.wire.modul.SessionManager;
import patrick.pramedia.wire.modul.VarGlobals;
import patrick.pramedia.wire.rv.CustomRVItemTouchListener;
import patrick.pramedia.wire.rv.RecyclerViewItemClickListener;

public class Group extends Fragment implements View.OnClickListener{

    //private AppBarLayout ab;
    private LinearLayout ct;

    private boolean isFabOpen = false;

    private FrameLayout mainframe;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private FloatingActionButton fab, fab_add, fab_refresh, fab_shared;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private RecyclerView recyclerView, recyclerViewS;
    private SessionManager ses = new SessionManager();
    private Globals g = new Globals();
    private SecurityAES128CBC s1;

    public static EditText txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private ArrayList<Kelompok> data = new ArrayList<>();
    private AdapterKelompok adapterKelompok;

    private ArrayList<Kelompok> dataS = new ArrayList<>();
    private AdapterKelompokS adapterKelompokS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        mainframe = (FrameLayout)  v.findViewById(R.id.mainframe);
        ft = getActivity().getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.mainframe, new STTFragment());
        ft.commit();

//        ab.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                if (Math.abs(i)-appBarLayout.getTotalScrollRange() == 0) {
//                    ct.setVisibility(View.VISIBLE);
//                } else {
//                    ct.setVisibility(View.INVISIBLE);
//                }
//            }
//        });

        txtSpeechInput = (EditText) v.findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) v.findViewById(R.id.btnPlaySpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = ((EditText)txtSpeechInput).getText().toString().toLowerCase();
                if (temp.substring(0, 4).equals("turn")&&temp.length()>8){
                    if(temp.substring(5, 7).equals("on")){
                        String groupname = temp.substring(8);
                        for(Kelompok k : AdapterKelompok.listKelompok){
                            if(k.getNama_kelompok().equals(groupname)){
                                setGroupingState(g.getServer()+"setGroupingState.php", k.getId_kelompok(), "1");
                                break;
                            }
                        }
                    }
                    else if(temp.substring(5, 8).equals("off")&&temp.length()>9){
                        String groupname = temp.substring(9);
                        for(Kelompok k : AdapterKelompok.listKelompok){
                            if(k.getNama_kelompok().equals(groupname)){
                                if(k.getNama_kelompok().equals(groupname)){
                                    setGroupingState(g.getServer()+"setGroupingState.php", k.getId_kelompok(), "0");
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });

        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        fab_add = (FloatingActionButton) v.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);

        fab_refresh = (FloatingActionButton) v.findViewById(R.id.fab_refresh);
        fab_refresh.setOnClickListener(this);

        fab_shared = (FloatingActionButton) v.findViewById(R.id.fab_shared);
        fab_shared.setOnClickListener(this);

        fab_open = AnimationUtils.loadAnimation(v.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(v.getContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(v.getContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(v.getContext(),R.anim.rotate_backward);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        adapterKelompok = new AdapterKelompok(data, getActivity(), recyclerView, new interfaceRVKelompok() {
            @Override
            public void click(int position) {
                Kelompok obj = data.get(position);
                String id = obj.getId_kelompok();
                String nama = obj.getNama_kelompok();
                boolean status = obj.getStatus_group();

                dialogGroup(id, nama, status);
            }
        });

        // Data Pegiriman
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterKelompok);
        adapterKelompok.notifyDataSetChanged();

        getOwnedGrouping(g.getServer()+"getOwnedGroupings.php");

        recyclerViewS = (RecyclerView) v.findViewById(R.id.rv_share);
        adapterKelompokS = new AdapterKelompokS(dataS, getActivity(), recyclerViewS);

        // Data Pegiriman
        RecyclerView.LayoutManager mLayoutManagerS = new LinearLayoutManager(getActivity());
        recyclerViewS.setLayoutManager(mLayoutManagerS);
        recyclerViewS.setItemAnimator(new DefaultItemAnimator());
        recyclerViewS.setAdapter(adapterKelompokS);
        adapterKelompokS.notifyDataSetChanged();

        getSharedGrouping(g.getServer()+"getSharedGroupings.php");

        return v;
    }

    private void setGroupingState(String url, final String grouping_id, final String device_state){
        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progress.dismiss();
                            if(response.equals("1")){
                                Toast.makeText(getContext(), "Item saved", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Item not saved", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    String iv = g.generateRandomString();
                    SecurityAES128CBC s1 = new SecurityAES128CBC(ses.getPreferences(getContext(),"KEY"),iv);
                    String enkrip_grouping_id = s1.encrypt(grouping_id);
                    String enkrip_device_state = s1.encrypt(device_state);

                    //Adding parameters to request
                    params.put("account_id", ses.getPreferences(getContext(),"ID"));
                    params.put("account_password", ses.getPreferences(getContext(),"PASS"));
                    params.put("grouping_id", enkrip_grouping_id);
                    params.put("device_state", enkrip_device_state);
                    params.put("iv", iv);

                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void animateFAB(){
        if(isFabOpen){
            fab.startAnimation(rotate_backward);

            fab_add.startAnimation(fab_close);
            fab_shared.startAnimation(fab_close);
            fab_refresh.startAnimation(fab_close);

            fab_add.setClickable(false);
            fab_shared.setClickable(false);
            fab_refresh.setClickable(false);

            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);

            fab_add.startAnimation(fab_open);
            fab_shared.startAnimation(fab_open);
            fab_refresh.startAnimation(fab_open);

            fab_add.setClickable(true);
            fab_shared.setClickable(true);
            fab_refresh.setClickable(true);

            isFabOpen = true;
        }
    }

    @Override
    public void onClick(View view) {
        if(view == fab){
            animateFAB();
        } else if(view == fab_add){
            DialogRoom();
        }else if(view == fab_refresh){
            getOwnedGrouping(g.getServer()+"getOwnedGroupings.php");
            getSharedGrouping(g.getServer()+"getSharedGroupings.php");
        }
        else if(view == fab_shared){
            DialogSharedRoom();
        }
    }


    private void DialogRoom() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_group);
        dialog.setTitle("Group Name");

        final EditText txtRoom = dialog.findViewById(R.id.txtRoom);

        final Button btnProses = dialog.findViewById(R.id.btnProses);
        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String iv = g.generateRandomString();
                    String room = txtRoom.getText().toString();
                    s1 = new SecurityAES128CBC(ses.getPreferences(getActivity(),"KEY"),iv);
                    String enkrip = s1.encrypt(room);
                    saveRoom(g.getServer()+"registerGrouping.php", enkrip,iv);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private String idDevice = "";
    private TextView txtDeviceName;
    private ArrayList<EntitasDevice> data_mydevice = new ArrayList<>();
    private AdapterChooseDevice adapterChooseDevice = new AdapterChooseDevice(data_mydevice);

    private ArrayList<RemoveDevice> data_mydevice_remove = new ArrayList<>();
    private AdapterRemoveDevice adapterRemoveDevice;

    private void dialogGroup(final String idroom, String name, boolean status) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_set_device);
        dialog.setTitle("Set Device Group");

        final TextView txtGroupName = (TextView) dialog.findViewById(R.id.txtGroupName);
        txtGroupName.setText(name);

        txtDeviceName = (TextView) dialog.findViewById(R.id.txtDeviceName);
        txtDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMyGroup(idroom);
            }
        });

        final Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idDevice.length() > 0){
                    try {
                        String iv = g.generateRandomString();
                        s1 = new SecurityAES128CBC(ses.getPreferences(getActivity(),"KEY"),iv);
                        String enkrip_room = s1.encrypt(idroom);
                        String enkrip_device = s1.encrypt(idDevice);

                        saveDeviceIntoRoom(g.getServer()+"registerGroupingDevice.php", idroom ,enkrip_room, enkrip_device,iv);
                        loaddata_device(g.getServer()+"getGroupingDevicesChoice.php", idroom);

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please fill device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final RecyclerView rv = (RecyclerView) dialog.findViewById(R.id.rv);
        adapterRemoveDevice = new AdapterRemoveDevice(data_mydevice_remove, getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapterRemoveDevice);
        adapterRemoveDevice.notifyDataSetChanged();

        loadremove_device(g.getServer()+"getRemoveDevices.php", idroom);

        dialog.show();
    }

    private void dialogMyGroup(String idroom) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_mygroup);
        dialog.setTitle("My Group");

        final RecyclerView rv = (RecyclerView) dialog.findViewById(R.id.rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapterChooseDevice);
        adapterChooseDevice.notifyDataSetChanged();
        rv.addOnItemTouchListener(new CustomRVItemTouchListener(getActivity(), rv, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                EntitasDevice obj = data_mydevice.get(position);
                String id = obj.getId();
                String nama = obj.getNama();

                idDevice = id;
                txtDeviceName.setText(nama);
                dialog.dismiss();
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        loaddata_device(g.getServer()+"getGroupingDevicesChoice.php", idroom);

        dialog.show();
    }

    private void loadremove_device(String url, final String idgroup){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // bersihkan array list
                            data_mydevice_remove.clear();

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                            for(int i=0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String g_id = jsonObject.optString("grouping_id").trim();
                                String d_id = jsonObject.optString("device_id").trim();
                                String d_name = jsonObject.optString("device_name").trim();

                                setDataRemove(g_id, d_id, d_name);
                            }
                            // close prosgress bar
                            adapterRemoveDevice.notifyDataSetChanged();

                            progress.dismiss();
                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("grouping_id", idgroup);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void setDataRemove(String group_id, String device_id, String device_name){
        RemoveDevice obj = new RemoveDevice(group_id, device_id, device_name);
        data_mydevice_remove.add(obj);
        adapterRemoveDevice.notifyDataSetChanged();
    }

    private void loaddata_device(String url, String idroom){
        final String id = idroom;
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        Log.d("AAA", response);
                        try {
                            // bersihkan array list
                            data_mydevice.clear();

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                            for(int i=0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.optString("id").trim();
                                String name = jsonObject.optString("name").trim();
                                setData(id, name);
                            }
                            // close prosgress bar
                            adapterChooseDevice.notifyDataSetChanged();

                            progress.dismiss();
                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                params.put("grouping_id", id);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void setData(String mac, String nama_ruang){
        EntitasDevice obj = new EntitasDevice();
        obj.setId(mac);
        obj.setNama(nama_ruang);
        data_mydevice.add(obj);
        adapterChooseDevice.notifyDataSetChanged();
    }

    private void saveRoom(String url, final String enkrip, final String iv){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // close prosgress bar
                            progress.dismiss();
                            // refresh rv utama
                            getOwnedGrouping(g.getServer()+"getOwnedGroupings.php");

                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                params.put("account_password", ses.getPreferences(getActivity(),"PASS"));
                params.put("grouping_name", enkrip);
                params.put("iv", iv);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void saveDeviceIntoRoom(String url, final String idroom, final String enkrip_room_id, final String enkrip_device_id, final String iv){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // close prosgress bar
                            progress.dismiss();
                            if(response.equals("1")){
                                Toast.makeText(getActivity(), "Device saved", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Device not saved", Toast.LENGTH_SHORT).show();
                            }
                            // refresh rv utama
                            loadremove_device(g.getServer()+"getRemoveDevices.php", idroom);

                            txtDeviceName.setText("");

                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                params.put("account_password", ses.getPreferences(getActivity(),"PASS"));
                params.put("grouping_id", enkrip_room_id);
                params.put("device_id", enkrip_device_id);
                params.put("iv", iv);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void getOwnedGrouping(String url){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // bersihkan array list
                            data.clear();

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                            for(int i=0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.optString("id").trim();
                                String name = jsonObject.optString("name").trim();
                                boolean status_group = jsonObject.optBoolean("state");
                                // insert data into array list
                                loadData(id, name, status_group);
                            }
                            // close prosgress bar
                            adapterKelompok.notifyDataSetChanged();
                            // close prosgress bar
                            progress.dismiss();
                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void loadData(String kode, String nama, boolean status){
        Kelompok obj = new Kelompok();
        obj.setId_kelompok(kode);
        obj.setNama_kelompok(nama);
        obj.setStatus_group(status);

        data.add(obj);
        adapterKelompok.notifyDataSetChanged();
    }

    private void getSharedGrouping(String url){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // bersihkan array list
                            dataS.clear();
                            Log.d("AAA", response);

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray jsonArray = jsonRootObject.optJSONArray("result");
                            for(int i=0; i< jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.optString("id").trim();
                                String name = jsonObject.optString("name").trim();
                                String email = jsonObject.optString("email").trim();
                                name = name + " - " + email;
                                boolean status_group = jsonObject.optBoolean("state");
                                // insert data into array list
                                loadDataS(id, name, status_group);
                            }
                            // close prosgress bar
                            adapterKelompokS.notifyDataSetChanged();
                            // close prosgress bar
                            progress.dismiss();
                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void loadDataS(String kode, String nama, boolean status){
        Kelompok obj = new Kelompok();
        obj.setId_kelompok(kode);
        obj.setNama_kelompok(nama);
        obj.setStatus_group(status);
        Log.d("AAAA", kode+nama+status);
        dataS.add(obj);
        adapterKelompokS.notifyDataSetChanged();
    }

    private void DialogSharedRoom() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_shared);
        dialog.setTitle("Shared Groups");

        final EditText etgrup = dialog.findViewById(R.id.etgrups);
        final EditText etemail = dialog.findViewById(R.id.etemails);
        final EditText etpass = dialog.findViewById(R.id.etpasswords);

        final Button btnadds = dialog.findViewById(R.id.btnadds);
        btnadds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String iv = g.generateRandomString();
                    String room = etgrup.getText().toString();
                    String oemail = etemail.getText().toString();
                    String opass = etpass.getText().toString();
                    s1 = new SecurityAES128CBC(ses.getPreferences(getActivity(),"KEY"),iv);

                    String unique_salt = VarGlobals.more_salt.substring(oemail.length()%VarGlobals.more_salt.length());
                    SecuritySHA256 s = new SecuritySHA256();
                    String password = s.hash(VarGlobals.header_salt+unique_salt+opass+oemail+VarGlobals.end_salt);

                    room = s1.encrypt(room);
                    oemail = s1.encrypt(oemail);

                    saveSharedRoom(g.getServer()+"registerSharedGrouping.php", room, oemail, password, iv);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    private void saveSharedRoom(String url, final String grup, final String email, final String pass, final String iv){
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..."); // Setting Message
        //progress.setTitle("ProgressDialog"); // Setting Title
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progress.show(); // Display Progress Dialog
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Extrak JSON
                        try {
                            // close prosgress bar
                            progress.dismiss();
                            // refresh rv utama
                            getOwnedGrouping(g.getServer()+"getOwnedGroupings.php");
                            getSharedGrouping(g.getServer()+"getSharedGroupings.php");
                        }catch (Exception e){
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "The server unreachable", Toast.LENGTH_SHORT).show();
                        // close prosgress bar
                        progress.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("account_id", ses.getPreferences(getActivity(),"ID"));
                params.put("account_password", ses.getPreferences(getActivity(),"PASS"));
                params.put("owner_email", email);
                params.put("owner_password", pass);
                params.put("grouping_name", grup);
                params.put("iv", iv);
                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}

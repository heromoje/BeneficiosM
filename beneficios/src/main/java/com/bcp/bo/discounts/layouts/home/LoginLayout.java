package com.bcp.bo.discounts.layouts.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.base.BaseLayout;
import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.AlertMsg;
import com.bcp.bo.discounts.general.Constants;
import com.bcp.bo.discounts.general.StyleApp;
import com.bcp.bo.discounts.general.Utils;
import com.bcp.bo.discounts.managers.FileManager;
import com.bcp.bo.discounts.model.Extension;
import com.bcp.bo.discounts.views.KenBurnsLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bcp.bo.service.Sender;
import bcp.bo.service.ServiceLauncher;
import bcp.bo.service.ServiceLauncher.IService;
import bcp.bo.service.model.request.Condition;
import bcp.bo.service.model.request.Login;
import bcp.bo.service.model.response.Category;
import bcp.bo.service.model.response.City;
import bcp.bo.service.model.response.Commerce;
import bcp.bo.service.model.response.PrivateToken;
import bcp.bo.service.model.response.Response;

public class LoginLayout extends BaseLayout implements IService {

    private static final boolean LOGIN_BUTTON_ENABLED = false;

    private static final int DNI_MAX_LENGTH = 8;

    private static final int ID_CONDITION_OK = 100;
    private static final int ID_CONDITION_CANCEL = 200;
    private static final int ID_WITHOUT_OFFERS = 200;

    public String firebaseToken;

    //views
    private EditText editText_Document;
    private EditText editText_Complement;
    private Button button_Extension;
    private ImageButton imageButton_Delete;
    private ImageButton imageButton_ComplementDelete;
    private final TextWatcher textWatcherComplement = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                imageButton_ComplementDelete.setVisibility(View.GONE);
            } else {
                imageButton_ComplementDelete.setVisibility(View.VISIBLE);
            }
            StyleApp.setStyle(editText_Document);
        }
    };
    private Button button_Login;
    /**
     * Listen for input text changes.
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editText_Document.setTextAppearance(editText_Document.getContext(), R.style.loginTextCic);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (editText_Document.getError() != null) {
                editText_Document.setError(null);
                editText_Document.setTextAppearance(editText_Document.getContext(), R.style.loginTextCic);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                imageButton_Delete.setVisibility(View.GONE);
            } else {
                imageButton_Delete.setVisibility(View.VISIBLE);
            }

            if (LOGIN_BUTTON_ENABLED) {
                //check login has the same length
                if (s.length() == DNI_MAX_LENGTH) {
                    button_Login.setEnabled(true);
                } else {
                    button_Login.setEnabled(false);
                }
            }
            StyleApp.setStyle(editText_Document);
        }
    };
    private KenBurnsLayout kenBurnsLayout;
    /**
     * Data source
     **/
    private String _authToken;
    private Login _login = new Login();
    private List<Category> _categories;
    private List<Commerce> _promotionsTop;
    private String _amountSaved;
    private List<City> _cities;
    private List<Extension> extensiones;
    private String _email;
    private String _phone;
    private boolean onBackground = false;
    private OnItemClickListener extensionItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            Extension extension = extensiones.get(position);
            button_Extension.setText(extension.getExtension() + " - " + extension.getShort());
            button_Extension.setError(null);
            _login.Extension = extension.getShort();
            _login.Type = extension.getType();
        }
    };
    private ListAdapter extensionListAdapter = new ListAdapter() {
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dlg_list_item_extension, parent, false);
            }
            final Extension extension = getItem(position);

            TextView tvCompany = (TextView) convertView.findViewById(R.id.textView_Extension);
            TextView tvAddress = (TextView) convertView.findViewById(R.id.textView_Short);

            tvCompany.setText(extension.getExtension());
            tvAddress.setText(extension.getShort());

            //apply style
            StyleApp.setStyle(convertView);

            return convertView;
        }

        @Override
        public int getCount() {
            return extensiones.size();
        }

        @Override
        public Extension getItem(int arg0) {
            return extensiones.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return extensiones.size() == 0;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int arg0) {
            return true;
        }
    };
    /**
     * Listen for soft keyboard actions.
     */
    private final OnEditorActionListener editorActionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Utils.hideKeyboard(getBaseLayoutActivity(), editText_Document);
                AlertMsg.showListDialog(getBaseLayoutActivity(),
                        getString(R.string.LGN_EXTENSION), extensionListAdapter, extensionItemClickListener, R.string.DSC_GO_BACK);
                //initLogin();
            }
            return true;
        }
    };

    public LoginLayout(BaseLayoutActivity baseLayoutActivity) {
        super(baseLayoutActivity);

    }

    @Override
    protected int getXMLToInflate() {
        return R.layout.lay_dsc_login;
    }

    @Override
    protected void initViews() {
        editText_Document = (EditText) view.findViewById(R.id.etCic);
        editText_Complement = (EditText) view.findViewById(R.id.etComp);
        button_Extension = (Button) view.findViewById(R.id.button_Extension);
        imageButton_Delete = (ImageButton) view.findViewById(R.id.button_Delete);
        imageButton_ComplementDelete = (ImageButton) view.findViewById(R.id.button_ComplementDelete);
        button_Login = (Button) view.findViewById(R.id.bLogin);
        kenBurnsLayout = (KenBurnsLayout) view.findViewById(R.id.kblBackground);

        if (ContextCompat.checkSelfPermission(getBaseLayoutActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getBaseLayoutActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 419);
        }
    }

    @Override
    protected void initBaseLayouts() {

    }

    @Override
    protected void initValues() {
        extensiones = new ArrayList<>();
        String[] extensionesItems = getBaseLayoutActivity().getResources().getStringArray(R.array.extensiones);
        for (String extensionItem : extensionesItems) {
            String[] values = extensionItem.split(",");
            Extension extension = new Extension();
            extension.setShort(values[0]);
            extension.setExtension(values[1]);
            extension.setType(values[2]);
            extensiones.add(extension);
        }

        final List<String> list = getKenBurnsImagePaths();
        if (list.isEmpty()) {
            kenBurnsLayout.addImage(R.drawable.lgn_bg_1);
            kenBurnsLayout.addImage(R.drawable.lgn_bg_2);
            /*kblBackground.addImage(R.drawable.lgn_bg_3);
            kblBackground.addImage(R.drawable.lgn_bg_4);
			kblBackground.addImage(R.drawable.lgn_bg_5);
			kblBackground.addImage(R.drawable.lgn_bg_6);
			kblBackground.addImage(R.drawable.lgn_bg_7);
			kblBackground.addImage(R.drawable.lgn_bg_8);
			kblBackground.addImage(R.drawable.lgn_bg_10);
			kblBackground.addImage(R.drawable.lgn_bg_11);
			kblBackground.addImage(R.drawable.lgn_bg_12);*/
            kenBurnsLayout.addImage(R.drawable.lgn_bg_13);
        } else {
            kenBurnsLayout.addImages(list);
        }

        //enabled button
        if (LOGIN_BUTTON_ENABLED) {
            //disable button
            button_Login.setEnabled(false);
        }

        StyleApp.setStyle(view);
    }

    @Override
    protected void initListeners() {
        editText_Document.addTextChangedListener(textWatcher);
        editText_Complement.addTextChangedListener(textWatcherComplement);
        button_Extension.setOnClickListener(clickListener);
        editText_Document.setOnEditorActionListener(editorActionListener);
        button_Login.setOnClickListener(clickListener);
        imageButton_Delete.setOnClickListener(clickListener);
        imageButton_ComplementDelete.setOnClickListener(clickListener);
    }

    @Override
    protected void init() {
        kenBurnsLayout.play();
        getFirebaseToken();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kenBurnsLayout.stop();
        onBackground = true;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void click(View view) {
        if (view.getId() == R.id.bLogin) {
            initLogin();
        } else if (view.getId() == R.id.button_Delete) {
            editText_Document.setText(null);
            imageButton_Delete.setVisibility(View.GONE);
        } else if (view.getId() == R.id.button_ComplementDelete) {
            editText_Complement.setText(null);
            imageButton_ComplementDelete.setVisibility(View.GONE);
        } else if (view.getId() == R.id.button_Extension) {
            AlertMsg.showListDialog(getBaseLayoutActivity(),
                    getString(R.string.LGN_EXTENSION), extensionListAdapter, extensionItemClickListener, R.string.DSC_GO_BACK);
        } else if (view.getId() == ID_CONDITION_OK) {
            /*AlertMsg.showLoadingDialog(getBaseLayoutActivity(), R.string.LOADING_CATEGORY);
            getBaseLayoutActivity().setLoadingMessage(R.string.LOADING_CATEGORY);
			ServiceLauncher.executeCategories(getBaseLayoutActivity().getBaseContext(),userLogedData.getJSONforCategories(),this);*/
        } else if (view.getId() == ID_CONDITION_CANCEL) {
            getBaseLayoutActivity().dismissDialog();
        } else if (view.getId() == ID_WITHOUT_OFFERS) {
            getBaseLayoutActivity().dismissDialog();
        }
    }

    @Override
    protected void handleMessageReceived(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    protected void dataReceived(Bundle extras) {

    }

    //-------------- LISTENERS ----------------------

    @Override
    protected void dataToSave(Bundle extras) {

    }

    @Override
    protected void dataToReturn(Bundle extras) {

    }

    @Override
    protected boolean keyPressed(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //logout
            getBaseLayoutActivity().finish();
            return true;

        } else {
            return false;
        }
    }

    //-------------- TRY TO LOGIN ---------------

    @Override
    public boolean unloadLayoutResult(Bundle data) {
        return false;
    }

    private void initLogin() {
        onBackground = false;

        // Store values at the time of the login attempt.
        String nroDocument = editText_Document.getText().toString();

        // Check for a valid nid.
        if (!isValidDni(nroDocument)) {
            imageButton_Delete.setVisibility(View.GONE);
            editText_Document.setTextAppearance(editText_Document.getContext(), R.style.loginTextCicError);

        } else if (button_Extension.getText().toString().equals(getBaseLayoutActivity().getResources().getString(R.string.LGN_EXTENSION))) {
            button_Extension.setError(getString(R.string.LGN_INVALID_EXTENSION));
        } else {
            //If soft key board is open, close it
            Utils.hideKeyboard(getBaseLayoutActivity(), editText_Document);

            AlertMsg.showLoadingDialog(getBaseLayoutActivity(), R.string.LOADING_VALIDATING_USER);

            _login.Token = BaseActivity.publicPrivateTokenCIC.PublicToken;
            _login.NroDocument = nroDocument;
            _login.Complement = editText_Complement.getText().toString();
            if (firebaseToken != "") {
                _login.PhoneToken = firebaseToken;
            }

            BaseActivity.document = nroDocument + " " + _login.Extension;

            SLauncher.loginUser(_login, this);

            //ServiceLauncher.executeAuthenticationUser(getBaseLayoutActivity().getBaseContext(),userLogedData.getJSONLogin(),this);
        }
    }

    public void initCondition() {
        BaseActivity.showLoading();
        Condition condition = new Condition();
        //HARDCORE
        condition.Condition = 1;
        condition.Email = _email;
        condition.Phone = Integer.valueOf(_phone);

        SLauncher.sendCondition(condition, this);
    }

    public void initCategories() {
        getBaseLayoutActivity().setLoadingMessage(R.string.LOADING_CATEGORY);
        BaseActivity.publicPrivateTokenCIC.City = "LP";
        SLauncher.loadCategories(BaseActivity.publicPrivateTokenCIC, this);
    }

    public void initPromotionsTop() {
        getBaseLayoutActivity().setLoadingMessage(R.string.LOADING_DEFAULT_CATEGORY);
        SLauncher.loadPromotionsTop(BaseActivity.publicPrivateTokenCIC, this);
    }

    public void loadCities() {
        getBaseLayoutActivity().setLoadingMessage(R.string.LOADING_CITIES);
        SLauncher.loadCities(BaseActivity.publicPrivateTokenCIC, this);
    }

    public void initAmountSaved() {
        getBaseLayoutActivity().setLoadingMessage(R.string.LOADING_PROMOTIONS);
        SLauncher.loadAmountSaved(BaseActivity.publicPrivateTokenCIC, this);
    }

    //-------------- KEN BURNS -------------------

    /**
     * Validate if the cic is a number and has CIC_LENGTH digits (added zeros to the left if needed).
     *
     * @param ci The cic to validate.
     * @return True if valid, false otherwise.
     */
    public boolean isValidDni(String ci) {
        if (ci.length() == 0) {
            editText_Document.setError(getString(R.string.LGN_EMPTY_ERROR));
            editText_Document.requestFocus();
            return false;

        } else if (ci.length() > DNI_MAX_LENGTH) {
            editText_Document.setError(getString(R.string.LGN_ERRONEOUS));
            editText_Document.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    /**
     * Get all image paths for ken burns widget.
     *
     * @return A list of absolute paths.
     */
    public List<String> getKenBurnsImagePaths() {
        List<String> list = new ArrayList<String>();

        FileManager fileManager = new FileManager();

        String[] internalPaths = fileManager.listAbsolutePathInternalStoragePrivateFile(getBaseLayoutActivity(), Constants.KEN_BURNS_PATH);
        String[] externalPaths = fileManager.listAbsolutePathExternalStoragePrivateFile(getBaseLayoutActivity(), Constants.KEN_BURNS_PATH);


        if (internalPaths != null) {
            list.addAll(Arrays.asList(internalPaths));
        }

        if (externalPaths != null) {
            list.addAll(Arrays.asList(externalPaths));
        }

        return list;
    }

    @Override
    public void onCompleted(boolean status, String message, String service, JSONObject jsonObject) {
        super.onCompleted(status, message, service, jsonObject);
        if (status) {
            if (service.equals(bcp.bo.service.ServiceLauncher.AuthenticationUser)) {
                Response<PrivateToken> responsePrivateToken = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<PrivateToken>>() {
                });
                if (responsePrivateToken.State) {
                    BaseActivity.publicPrivateTokenCIC.PrivateToken = responsePrivateToken.Data.PrivateToken;
                    BaseActivity.publicPrivateTokenCIC.CIC = responsePrivateToken.Data.CIC;
                    BaseActivity.privateToken = responsePrivateToken.Data;
                    BaseActivity.amountSaved = responsePrivateToken.Data.AmountSaved;
                    if (responsePrivateToken.Data.Condition == 1) {
                        initCategories();
//                        _promotionsTop = new ArrayList<>();
//                        initAmountSaved();
                    } else {
                        getBaseLayoutActivity().dismissDialog();
                        AlertMsg.showCondition(this, this);
                    }
                } else {
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), responsePrivateToken.Message);
                }
            } else if (service.equals(ServiceLauncher.AuthenticationCondition)) {
                Response<Boolean> responseCondition = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<Boolean>>() {
                });
                if (responseCondition.State && responseCondition.Data) {
                    initCategories();
                } else {
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), responseCondition.Message);
                }
            } else if (service.equals(ServiceLauncher.UserCategories)) {
                Response<List<Category>> responseCategories = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<List<Category>>>() {
                });
                if (responseCategories.State) {
                    _categories = responseCategories.Data;
                    if (responseCategories.Data.size() > 0) {
                        loadCities();
//                        initPromotionsTop();
                    } else {
                        getBaseLayoutActivity().dismissDialog();
                        AlertMsg.showWarningDialog(getBaseLayoutActivity(), R.string.ERROR_SECTION_EXPIRED);
                    }

                } else {
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), responseCategories.Message);
                }
            } else if (service.equals(ServiceLauncher.UserPromotionsTop)) {
                Response<List<Commerce>> responsePromotionsTop = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<List<Commerce>>>() {
                });

                if (responsePromotionsTop.Data != null) {
                    _promotionsTop = responsePromotionsTop.Data;
                    initAmountSaved();
                } else {
                    _promotionsTop = new ArrayList<>();
                    initAmountSaved();
                }
                /*if(responsePromotionsTop.State){
                    _promotionsTop=responsePromotionsTop.Data;
					initAmountSaved();
                }else{
                    getBaseLayoutActivity().dismissDialog();
					AlertMsg.showErrorDialog(getBaseLayoutActivity(), responsePromotionsTop.Message);
                }*/

            } else if (service.equals(ServiceLauncher.UserCities)) {
                Response<List<City>> responseCities = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<List<City>>>() {
                });
                if (responseCities.State) {
                    _cities = responseCities.Data;
                    initAmountSaved();
//                    getBaseLayoutActivity().dismissDialog();
//                    HomeLayout homeLayout = new HomeLayout(getBaseLayoutActivity(), _categories, _promotionsTop, BaseActivity.privateToken, BaseActivity.document, _amountSaved);
//                    replaceLayout(homeLayout, false);

                } else {
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), responseCities.Message);
                }
            } else if (service.equals(ServiceLauncher.UserAmountSaved)) {
                Response<String> responseAmountSaved = Sender.fromJSON(jsonObject.toString(), new TypeReference<Response<String>>() {
                });
                if (responseAmountSaved.State) {
                    _amountSaved = responseAmountSaved.Data;
                    _promotionsTop = new ArrayList<>();
                    getBaseLayoutActivity().dismissDialog();
                    HomeLayout homeLayout = new HomeLayout(getBaseLayoutActivity(), _cities, _categories, _promotionsTop, BaseActivity.privateToken, BaseActivity.document, _amountSaved);
                    replaceLayout(homeLayout, false);

                } else {
                    getBaseLayoutActivity().dismissDialog();
                    AlertMsg.showErrorDialog(getBaseLayoutActivity(), responseAmountSaved.Message);
                }
            }
        } else {//ERROR DE CONEXION
            AlertMsg.showErrorDialog(getBaseLayoutActivity(), message);
        }
    }


    private void getFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String TAG = "FIREBASE::::::::::::";
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        firebaseToken = task.getResult().getToken();
                        // Log and toast
                        /*
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                         */
                    }
                });
    }

    @Override
    public void onCancelled() {

    }
}
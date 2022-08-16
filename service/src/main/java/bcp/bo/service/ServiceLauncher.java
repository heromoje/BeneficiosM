package bcp.bo.service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import bcp.bo.service.model.request.Application;
import bcp.bo.service.model.request.Condition;
import bcp.bo.service.model.request.ListPromotions;
import bcp.bo.service.model.request.Login;
import bcp.bo.service.model.request.PublicPrivateTokenCIC;
import bcp.bo.service.model.request.RatingTicket;

/**
 * Created by BC2078 on 10/16/2015.
 */
public class ServiceLauncher {
    public static final String Authentication = "/authentication";
    public static final String AuthenticationUser = "/authentication/user";
    public static final String AuthenticationCondition = "/authentication/condition";
    public static final String UserCategories = "/user/categories";
    public static final String UserPromotionsTop = "/user/topPromotions";
    public static final String UserPromotions = "/user/promotions";
    public static final String UserContacts = "/user/contacts";
    public static final String UserRatingTicket = "/user/ratingticket";
    public static final String UserAmountSaved = "/user/amountSaved";
    public static final String UserCities = "/user/cities";
    //Cambios 11/18/2019
    public static final String RegisterNotificationToken = "/notification/registerToken";
    public static final String UpdateNotificationToken = "/notification/updateToken";
    //Fin Cambios 11/18/2019

    //GOOGLE
    //GOOGLE MAPS API
    public static final String GoogleMapsApi = "http://maps.googleapis.com/maps/api";
    public static final String ResolveRoute = "/directions/json";
    private static final int READ_TIMEOUT = 20000;
    private static final int CONNECT_TIMEOUT = 20000;
    public String Host;

    public ServiceLauncher(String host) {
        Host = host;
    }

    public void authenticate(Application application, IService callback) {
        SendPOST(application, Authentication, callback);
    }

    public void loginUser(Login login, IService callback) {
        SendPOST(login, AuthenticationUser, callback);
    }

    public void sendCondition(Condition condition, IService callback) {
        SendPOST(condition, AuthenticationCondition, callback);
    }

    public void loadCategories(PublicPrivateTokenCIC publicPrivateTokenCIC, IService callback) {
        SendPOST(publicPrivateTokenCIC, UserCategories, callback);
    }

    public void loadPromotionsTop(PublicPrivateTokenCIC publicPrivateTokenCIC, IService callback) {
        SendPOST(publicPrivateTokenCIC, UserPromotionsTop, callback);
    }

    public void loadAmountSaved(PublicPrivateTokenCIC publicPrivateTokenCIC, IService callback) {
        SendPOST(publicPrivateTokenCIC, UserAmountSaved, callback);
    }

    public void loadCities(PublicPrivateTokenCIC publicPrivateTokenCIC, IService callback) {
        SendPOST(publicPrivateTokenCIC, UserCities, callback);
    }

    public void loadCategory(ListPromotions listPromotions, IService callback) {
        SendPOST(listPromotions, UserPromotions, callback);
    }

    public void sendRating(RatingTicket ratingTicket, IService callback) {
        SendPOST(ratingTicket, UserRatingTicket, callback);
    }

    //GENERIC JSON-OBJECTS POST
    public void SendPOST(Object request, String service, IService callback) {
        RequestPOSTTask task = new RequestPOSTTask(Host, service, request, callback);
        task.execute();
    }

    public void ResolveRoute(double latIni, double lonIni, double latEnd, double lonEnd, IService callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("origin", latIni + "," + lonIni);
        params.put("destination", latEnd + "," + lonEnd);
        params.put("sensor", "false");
        params.put("mode", "driving");
        RequestGETTask task = new RequestGETTask(GoogleMapsApi, ResolveRoute, params, callback);
        task.execute();
    }

    public interface IService {
        void onCompleted(boolean status, String message, String service, JSONObject jsonObject);

        void onCancelled();
    }

    private static class RequestPOSTTask extends AsyncTask<Object, String, JSONObject> {
        public boolean Status;
        public String Message;

        private String Host;
        private String Service;
        private IService Callback;
        private Object ObjSend;

        private JSONObject JsonResult;

        public RequestPOSTTask(String host, String service, Object objSend, IService callback) {
            Host = host;
            Service = service;
            ObjSend = objSend;
            Callback = callback;
        }

        private static String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected JSONObject doInBackground(Object... params) {
            if (SendPOST(ObjSend)) {
                return JsonResult;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (JsonResult != null) {
                Callback.onCompleted(true, "OK", Service, JsonResult);
                return;
            }
            Callback.onCompleted(false, Message, Service, null);
        }

        @Override
        protected void onCancelled(JSONObject result) {
            Callback.onCancelled();
        }

        private boolean SendPOST(Object objSend) {
            if (ObjSend instanceof Sender) {
                String jsonResult;
                Sender sender = (Sender) ObjSend;
                HttpURLConnection conn;
                try {
                    //constants
                    URL url = new URL(Host + Service);
                    Log.d("ENDPOINT:", url.getPath());
                    String jsonToSend = sender.toJSON();
                    Log.d("REQUEST:", jsonToSend);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT /*milliseconds*/);
                    conn.setConnectTimeout(CONNECT_TIMEOUT /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(jsonToSend.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json");
                    //conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    //setup send
                    OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(jsonToSend.getBytes());
                    //clean up
                    os.flush();

                    //do somehting with response
                    InputStream is = conn.getInputStream();
                    String content = convertInputStreamToString(is);
                    Log.d("RESPONSE:", content);
                    JsonResult = new JSONObject(content);
                    return true;
                } catch (ConnectException e) {
                    Message = "No se puede establecer conexión con el servidor.";
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    Message = "No se pudo conectar al servidor.";
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    Message = "No se puede conectar al servidor.";
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    Message = "Tiempo de espera agotado.";
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    Message = "No se pudo establecer conexión con el servidor.";
                    e.printStackTrace();
                } catch (IOException e) {
                    Message = "No se puede leer la respuesta del servidor.";
                    e.printStackTrace();
                } catch (JSONException e) {
                    Message = "No se pueden leer los datos de respuesta.";
                    e.printStackTrace();
                } catch (Exception e) {
                    Message = "No se pudo establecer conexión.";
                    e.printStackTrace();

                } finally {
                    //conn.disconnect();
                }
            }
            return false;
        }
    }

    private static class RequestGETTask extends AsyncTask<Object, String, JSONObject> {
        public boolean Status;
        public String Message;

        private String Host;
        private String Service;
        private IService Callback;
        private HashMap<String, String> Params;

        private JSONObject JsonResult;

        public RequestGETTask(String host, String service, HashMap<String, String> params, IService callback) {
            Host = host;
            Service = service;
            Params = params;
            Callback = callback;
        }

        private static String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected JSONObject doInBackground(Object... params) {
            if (SendPOST(Params)) {
                return JsonResult;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (JsonResult != null) {
                Callback.onCompleted(true, "OK", Service, JsonResult);
                return;
            }
            Callback.onCompleted(false, Message, Service, null);
        }

        @Override
        protected void onCancelled(JSONObject result) {
            Callback.onCancelled();
        }

        private boolean SendPOST(HashMap<String, String> params) {
            HttpURLConnection conn;
            try {
                String paramsString = "?";
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramsString += entry.getKey() + "=" + entry.getValue() + "&";
                }
                paramsString = paramsString.substring(0, paramsString.length() - 1);
                byte[] postData = paramsString.getBytes();

                //constants
                URL url = new URL(Host + Service + paramsString);

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                /*conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset","utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postData.length));*/

                //open
                conn.connect();

                //setup send
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                //os.write(postData);
                //clean up
                os.flush();

                //do somehting with response
                InputStream is = conn.getInputStream();
                String content = convertInputStreamToString(is);
                JsonResult = new JSONObject(content);
                return true;
            } catch (ConnectException e) {
                Message = "No se puede establecer conexión con el ervido.";
                e.printStackTrace();
            } catch (UnknownHostException e) {
                Message = "No se pudo conectar al servidor.";
                e.printStackTrace();
            } catch (MalformedURLException e) {
                Message = "No se puede conectar al servidor.";
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                Message = "Tiempo de espera agotado.";
                e.printStackTrace();
            } catch (ProtocolException e) {
                Message = "No se pudo establecer conexión con el servidor.";
                e.printStackTrace();
            } catch (IOException e) {
                Message = "No se puede leer la respuesta del servidor.";
                e.printStackTrace();
            } catch (JSONException e) {
                Message = "No se pueden leer los datos de respuesta.";
                e.printStackTrace();
            } catch (Exception e) {
                Message = "No se pudo establecer conexión.";
                e.printStackTrace();

            } finally {
                //conn.disconnect();
            }
            return false;
        }
    }
}
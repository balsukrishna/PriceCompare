package com.krishna.androidproj.goldcomp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	//default method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   //     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    //    StrictMode.setThreadPolicy(policy);
    }

    //Default method
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void closeapp(View view) {
      this.finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
            	new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("balsukrishna@gmail.com")
                .setPositiveButton(android.R.string.ok, null)
                .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /* Called when the user clicks the Refresh button */
    public void refreshData(View view) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
    	   	
    
    	//get resource id--from avtivity_main.xml
    	int clickedid =    view.getId();
    	
    	//get the resource name. i.e edit_hdfc, or btn_uob etc
    	String resource_Name = getResources().getResourceEntryName(clickedid);
    	
    	// String manipulation to get source name. eg: uob, hdfc
    	String source_Name   = getSourceName(resource_Name);
    	
    	//get link by appending link_ to source name
    	URL source_Link   = getSourceLink(source_Name);
    	
    	//get data by sending http request
    	//double  latest_Response = getlatestresponse(source_Link,source_Name);
    	
    	//get the edittext view corresponding to clicked nutton
    	EditText editText = getRelatedEditText(source_Name);
    	
    	//update the latest response value to the edittext view
    	new UpdateData(editText,source_Name,this).execute(source_Link);
    	
    }
    
    //String manipulation to get source name
    //input : hdfc
    //eg: edit_hdfc, uob etc
    private EditText getRelatedEditText(String sourceName)
    {
    String temp = "edit_" + sourceName;
    int id = getResources().getIdentifier(temp, "id", getPackageName());
    EditText editT = (EditText) findViewById(id);
    return editT;
    }
    
	 //String manipulation to get source name
    //input : bt_hdfc
    //eg: hdfc, uob etc
    private String getSourceName(String resourceName)
    {
    	String sourceName = resourceName.substring(resourceName.indexOf('_') + 1);
    	return sourceName;
    	
    }
    
    //Send http request to link and get response
    public void paintResponse(double data, EditText editText)
    {
    	CharSequence cs = Double.toString(data);
    	editText.setText(cs);
    }
    
    //String manipulation to get link 
    private URL getSourceLink(String sourceName) throws MalformedURLException
    {
    	String temp = "link_" + sourceName ;
    	String templink =  (String) getResources().getText(getResources().getIdentifier(temp, "string", getPackageName()));
   


    	//String xmlst = tmp.toString();
        //Document doc = Jsoup.parse(xmlst);
        //Element  e= doc.select("a").first();
//        String templink = e.attr("href");
        return new URL(templink);
    	
    }
    
    // Get the response from link
    @SuppressWarnings("unused")
	private double getlatestresponse(URL sourceLink,String sourceName) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
    {
    	double data;
    	//Assign the source name to get relative xpath
    	Parser prs = new Parser(sourceName);
    	data = prs.getResponse(sourceLink);
    	prs.close();
    	return data;
    }




}
    
    //---------------------------------------
    //Creating Async task class to run all updates asunchronusly
    class UpdateData extends AsyncTask<URL, Void, String> {
    	
    	public EditText recedittext = null;
    	public String sourceName = "";
    	Activity act = null;
    	
    	   public UpdateData(EditText edittext,String sourcename , Activity activity) {
    	        super();
    	        recedittext = edittext;
    	        sourceName = sourcename;
    	        act = activity;
    	    }
	      
		
		   protected void onPostExecute(String val) {
			   
			   CharSequence cs = val;
			   recedittext.setText(cs);
			   //I have alazy ass to write this elegantly
				if (sourceName.equalsIgnoreCase("uob") || sourceName.equalsIgnoreCase("hdfc"))
				{
					EditText temp;
			   	if (sourceName.equalsIgnoreCase("uob") )
		    		
		    	{
			   		
			   		int temp1 = act.getResources().getIdentifier("edit_hdfc", "id", act.getPackageName());
		    		  temp = (EditText) act.findViewById(temp1);
		  
		    		  
		    	} else  {
		    		   int temp1 = act.getResources().getIdentifier("edit_uob", "id", act.getPackageName());
			    		  temp = (EditText) act.findViewById(temp1);
			    	
		   		}		
			    		  
		    		int temp2 = act.getResources().getIdentifier("edit_uobrupee", "id", act.getPackageName());
		    		 EditText edituobrupee = (EditText) act.findViewById(temp2);
		    		  
		    	
				double y = 0;
				double x = Double.parseDouble(val);
				try {
				  y = Double.parseDouble(temp.getText().toString());
				}
				catch (Exception ex){
					
				}
				finally {
					double temp3 = x*y;
					String last = String.valueOf(temp3);
					edituobrupee.setText(last.substring(0, last.indexOf('.')+1));
				}
				}
	        }
		
		   protected void onPreExecute() {
			   
		    	paintWaitResponse(recedittext);
		    	if (sourceName.equalsIgnoreCase("uob") || sourceName.equalsIgnoreCase("hdfc"))
		    		
		    	{
		    		int temp3 = act.getResources().getIdentifier("edit_uobrupee", "id", act.getPackageName());
		    		 EditText edituobrupee = (EditText) act.findViewById(temp3);
		    		  paintWaitResponse(edituobrupee);
		    	}
		    	
	        }
		   
			    
		    //Send http request to link and get response
		    public void paintWaitResponse(EditText editText)
		    {
	    	CharSequence cs = "fetching...";
		    	editText.setText(cs);
		    }
	 
		    


			@Override
			protected String doInBackground(URL... params) {
				
				double data = 0;
		       	//Assign the source name to get relative xpath
		       	Parser prs = new Parser(sourceName);
		       	try {
					data = prs.getResponse(params[0]);
				} catch (IOException e) {
					e.printStackTrace();
				}
		       	prs.close();
		       	
		       	return String.valueOf(data);
			}


		
    }
    
    

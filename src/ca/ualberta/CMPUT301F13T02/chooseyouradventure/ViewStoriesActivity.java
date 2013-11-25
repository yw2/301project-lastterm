/*
* Copyright (c) 2013, TeamCMPUT301F13T02
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without modification,
* are permitted provided that the following conditions are met:
* 
* Redistributions of source code must retain the above copyright notice, this
* list of conditions and the following disclaimer.
* 
* Redistributions in binary form must reproduce the above copyright notice, this
* list of conditions and the following disclaimer in the documentation and/or
* other materials provided with the distribution.
* 
* Neither the name of the {organization} nor the names of its
* contributors may be used to endorse or promote products derived from
* this software without specific prior written permission.
* 
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
* ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ca.ualberta.CMPUT301F13T02.chooseyouradventure;



import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import ca.ualberta.CMPUT301F13T02.chooseyouradventure.elasticsearch.ESHandler;

/**
 * The main activity of the application. Displays a list of stories to read. <br />
 * <br />
 * In this activity a reader can:
 * <ol>
 *     <li> Click a story to begin reading at the first page </li>  
 *     <li> Long click a story to cache it to local storage </li>
 *     <li> Search for stories </li>
 * </ol>
 * In this activity an author can: 
 * <ol>
 *     <li> Add a new story </li>
 *     <li> Long click a story to edit the story </li>
 * </ol>
 * 
 * The ViewStoriesActivity is a view of the application.
 * 
 * TODO Search needs to be implemented
 */

public class ViewStoriesActivity extends Activity {
	private ListView mainPage;
	private Button createNew;
	private Button searchButton;
	private Button randomStoryButton;
	private Button refreshButton;
	ArrayList<String> storyText = new ArrayList<String>();
	ArrayList<Story> storyList = new ArrayList<Story>();
	private ControllerApp app; 
	private StoryGUIs gui;
	
	
	private Handler eshandler = new ESHandler();
	private Handler dbhandler = new DBHandler(this);
	private static final int HELP_INDEX = 0;
	
	ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_stories_activity);
        mainPage = (ListView) findViewById(R.id.mainView);
        createNew = (Button) findViewById(R.id.createButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        randomStoryButton = (Button) findViewById(R.id.randomButton);
        refreshButton = (Button) findViewById(R.id.button1);
        createNew.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                createStory();
            }
        });
        refreshButton.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                refresh();
            }
        });
        
        searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSearchRequested();
			}
        	
        });
        
        randomStoryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onRandomStory();
				
			}
        	
        });
        
        app = (ControllerApp) getApplication();
        gui = new StoryGUIs(app, this);
        
        
		try {
			storyList =  eshandler.getAllStories();
			storyText = app.updateView(storyList, storyText);
		} catch (HandlerException e1) {
			e1.printStackTrace();
		}
		adapter = new ArrayAdapter<String>(this,
				R.layout.list_item_base, storyText);
		mainPage.setAdapter(adapter);
		
		/**
		 * method to restructure Click and longClick listeners to work in a list view
		 *  directly based on http://android.konreu.com/developer-how-to/click-long-press-event-listeners-list-activity/
		 */
		mainPage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long listNum) {
		        try {
					onListItemClick(v,pos,listNum);
				} catch (HandlerException e) {
					e.printStackTrace();
				}
		    }
		});

		mainPage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long listNum) {
		        return onLongListItemClick(v,pos,listNum);
		    }
		});
		
        
    }
    
    protected void onRandomStory() {
		try {
			Story random = eshandler.getRandomStory();
			app.jump(ViewPageActivity.class, random, random.getFirstpage());
		} catch (HandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onResume() {
        super.onResume();
        refresh();
    }
        


    /**
     * Inflate the options menu; this adds items to the action bar if it is present 
     * 
     *  @param menu The menu to inflate
     *  @return Success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
    	getMenuInflater().inflate(R.menu.view_stories, menu);

		MenuItem help = menu.add(0, HELP_INDEX, HELP_INDEX, getString(R.string.help));
		help.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

	/**
	 * Callback for clicking an item in the menu.
	 * 
	 * @param item The item that was clicked
	 * @return Success
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
		switch (item.getItemId()) {
		case HELP_INDEX:

			AlertDialog dialog = HelpDialogFactory.create(R.string.view_stories_help, this);
			dialog.show();
	        
			break;
		}
		return true;
    }
    
	protected void onListItemClick(View v, int pos, long id) throws HandlerException {	
		
	    app.jump(ViewPageActivity.class, storyList.get(pos), storyList.get(pos).getFirstpage());
	    
	}
	
	public boolean onLongListItemClick(View v, int pos, long id) { 
    	storyMenu(pos);
        return true;
    }
    
    /**
     * The options menu displayed when the user longClicks a story
     * @param v The view of the longClicked story
     */
	public void storyMenu(int pos){
			Story story = storyList.get(pos);
			AlertDialog builder = gui.storyMenuGUI(story, eshandler, dbhandler);
            builder.show();
        }


    
    /**
     * A pop up menu for creating a new story. it Simply asks for a title and then builds some framework before passing off to the Edit Story mode.
     */
    private void createStory(){

    	AlertDialog builder = gui.createStoryGUI();
        builder.show();
    }
    
    /**
     * Refreshes the list of stories by getting a new list from elastic search
     * and displaying it.
     */
    public void refresh(){
    
    	try {
        	storyList = eshandler.getAllStories();
        	storyList.addAll(dbhandler.getAllStories());
			storyText = app.updateView(storyList, storyText);
		} catch (HandlerException e1) {
			e1.printStackTrace();
		}
        adapter.notifyDataSetChanged();
		
    }
    
    /**
     * Sets the handler
     * 
     * @param handler The handler to use
     */
    public void setHandler(Handler handler) {
    	eshandler = handler;
    }
    

}

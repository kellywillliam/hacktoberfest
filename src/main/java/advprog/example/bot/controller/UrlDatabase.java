package advprog.example.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class UrlDatabase {
    private Map<String, FakeNews> listUrl;
    private final String DATA_PATH = "url.csv";
    private BufferedWriter writer;
    private BufferedReader reader;
    private FileWriter fileWriter;

    public UrlDatabase() {
        listUrl = new HashMap<>();
        this.loadingAllData();
    }

    private void loadingAllData() {
        try {
            //TODO load from csv database
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectFakeNews(String input) {
        String[] arr = input.split(",");
        //TODO Collect FakeNews from csv database
        addToList(url, types);
    }
	
	public synchronized String checkUrl(String cmd, String url){
		String hasil = "http://" + url + "not found in database\n";
		//TODO check url for private chat
		return hasil;
	}
	
	public synchronized String checkGroupUrl(String url){
		String hasil = "";
		//TODO check in Map given an url for group chat
		return hasil;
	}

    public synchronized void addFakeNews(String url, String type) {
        Set<String> types = new HashSet<>();
        types.add(type);
        this.addToList(url, types);
        saveToCsv(url, type);
    }

    private synchronized void addToList(String url, Set<String> types) {
        //TODO add new item(url -> FakeNews) to Map
    }

    public synchronized void saveToCsv(String url, String type) {
        try {
            //TODO write to csv
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

package com.dcu.wfd.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.dcu.wfd.common.vo.BundesligaDataStorage;
import com.dcu.wfd.common.vo.EplDataStorage;
import com.dcu.wfd.common.vo.LaligaDataStorage;
import com.dcu.wfd.common.vo.SerieaDataStorage;
import com.dcu.wfd.crawling.module.bundesliga.BundesligaMatchScheduleCrawling;
import com.dcu.wfd.crawling.module.bundesliga.BundesligaPlayerAsistRankCrawling;
import com.dcu.wfd.crawling.module.bundesliga.BundesligaPlayerRankCrawling;
import com.dcu.wfd.crawling.module.bundesliga.BundesligaTeamRankCrawling;
import com.dcu.wfd.crawling.module.epl.EplMatchScheduleCrawling;
import com.dcu.wfd.crawling.module.epl.EplPlayerAsistRankCrawling;
import com.dcu.wfd.crawling.module.epl.EplPlayerRankCrawling;
import com.dcu.wfd.crawling.module.epl.EplTeamRankCrawling;
import com.dcu.wfd.crawling.module.laliga.LaligaMatchScheduleCrawling;
import com.dcu.wfd.crawling.module.laliga.LaligaPlayerAsistRankCrawling;
import com.dcu.wfd.crawling.module.laliga.LaligaPlayerRankCrawling;
import com.dcu.wfd.crawling.module.laliga.LaligaTeamRankCrawling;
import com.dcu.wfd.crawling.module.main.NaverSportsNewsLatestCrawling;
import com.dcu.wfd.crawling.module.main.TodayMatchScheduleCrawling;
import com.dcu.wfd.crawling.module.seriea.SerieaMatchScheduleCrawling;
import com.dcu.wfd.crawling.module.seriea.SerieaPlayerAsistRankCrawling;
import com.dcu.wfd.crawling.module.seriea.SerieaPlayerRankCrawling;
import com.dcu.wfd.crawling.module.seriea.SerieaTeamRankCrawling;

public class CrawlingThread extends Thread {
	
	// 크롤링 시간담을 상수 선언. 60분 마다 크롤링.
	private final Object[][] STANDARD_TIMES = {
			{"naverSportsNewsLatest", 60*60}, 
			{"todayMatchSchedule", 60*60}, 
			
			{"todayEplMatchSchedule", 60*60},
			{"eplTeamRank", 60*60},
			{"eplPlayerRank", 60*60},
			{"eplPlayerAsistRank", 60*60},
			
			{"todayLaligaMatchSchedule", 60*60},
	        {"laligaTeamRank", 60*60},
	        {"laligaPlayerRank", 60*60},
	        {"laligaPlayerAsistRank", 60*60},
	        
	        {"todayBundesligaMatchSchedule", 60*60},
	        {"bundesligaTeamRank", 60*60},
	        {"bundesligaPlayerRank", 60*60},
	        {"bundesligaPlayerAsistRank", 60*60},
	        
	        {"todaySerieaMatchSchedule", 60*60},
	        {"serieaTeamRank", 60*60},
	        {"serieaPlayerRank", 60*60},
	        {"serieaPlayerAsistRank", 60*60}
	}; 
	
	
	@Override
	public void run() {
		
		while(true) {
			try {
				
				NaverSportsNewsLatestCrawling nsnlc = new NaverSportsNewsLatestCrawling();
				TodayMatchScheduleCrawling tmsc = new TodayMatchScheduleCrawling();
				
				
				EplMatchScheduleCrawling emsc = new EplMatchScheduleCrawling();
				EplTeamRankCrawling etrc = new EplTeamRankCrawling();
				EplPlayerRankCrawling eprc = new EplPlayerRankCrawling();
				EplPlayerAsistRankCrawling eparc = new EplPlayerAsistRankCrawling();
				
				LaligaMatchScheduleCrawling lmsc = new LaligaMatchScheduleCrawling();
	            LaligaTeamRankCrawling ltrc = new LaligaTeamRankCrawling();
	            LaligaPlayerRankCrawling lprc = new LaligaPlayerRankCrawling();
	            LaligaPlayerAsistRankCrawling lparc = new LaligaPlayerAsistRankCrawling();
	            
	            BundesligaMatchScheduleCrawling bmsc = new BundesligaMatchScheduleCrawling();
                BundesligaTeamRankCrawling btrc = new BundesligaTeamRankCrawling();
                BundesligaPlayerRankCrawling bprc = new BundesligaPlayerRankCrawling();
                BundesligaPlayerAsistRankCrawling bparc = new BundesligaPlayerAsistRankCrawling();
                
                SerieaMatchScheduleCrawling smsc = new SerieaMatchScheduleCrawling();
                SerieaTeamRankCrawling strc = new SerieaTeamRankCrawling();
                SerieaPlayerRankCrawling sprc = new SerieaPlayerRankCrawling();
                SerieaPlayerAsistRankCrawling sparc = new SerieaPlayerAsistRankCrawling();
				
				
				for(Object [] STANDARD_TIME : STANDARD_TIMES) {
					
					String crawlingDataName = (String) STANDARD_TIME[0];
					int crawlingDataTimes = (int) STANDARD_TIME[1];
					
					// naverSportsNewsLatest 크롤링 
					if(crawlingDataName.equals("naverSportsNewsLatest")) {
						
						// DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
						if(EplDataStorage.getNaverSportsNewsLatestData() == null) {
							// data에 크롤링하여 데이터를 넣고...
							ArrayList<HashMap<String, String>> data = nsnlc.naverSportsNewsLatestCrawling();
							// 만약 크롤링한 데이터가 있으면...
							if(data != null && data.size() > 0) {
								// EplDataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
								EplDataStorage.setNaverSportsNewsLatestData(data);
								EplDataStorage.setNaverSportsNewsLatestCrawlingTime(new Date());
								System.out.println("크롤링~");
							}
						} else { // EplDataStorage VO 변수에 데이터가 들어 있다면...
								// 크롤링된 시간을 변수에 담고...
								Date oldNaverSportsNewsLatestCrawlingTime = EplDataStorage.getNaverSportsNewsLatestCrawlingTime();
								// 현재크롤링한시간 변수에 담고...
								Date currentNaverSportsNewsLatestCrawlingTime = new Date();
								
								// 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
								long difTimes = (currentNaverSportsNewsLatestCrawlingTime.getTime() - oldNaverSportsNewsLatestCrawlingTime.getTime()) / 1000; // 초
								
								System.out.println("difTimes: " +difTimes);
								
								// 크롤링 할 
								if(difTimes > crawlingDataTimes) {
									ArrayList<HashMap<String, String>> data = nsnlc.naverSportsNewsLatestCrawling();
									if(data != null && data.size() > 0) {
										System.out.println("크롤링링~");
										EplDataStorage.setNaverSportsNewsLatestData(data);
										EplDataStorage.setNaverSportsNewsLatestCrawlingTime(new Date());
									}
								}
						}
						
					} else if(crawlingDataName.equals("todayMatchSchedule")) {
						// DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
						if(EplDataStorage.getTodayMatchScheduleData() == null) {
							// data에 크롤링하여 데이터를 넣고...
							ArrayList<HashMap<String, String>> data = tmsc.todayMatchScheduleCrawling();
							// 만약 크롤링한 데이터가 있으면...
							if(data != null && data.size() > 0) {
								// EplDataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
								EplDataStorage.setTodayMatchScheduleData(data);
								EplDataStorage.setTodayMatchScheduleCrawlingTime(new Date());
							} else {
								EplDataStorage.setTodayMatchScheduleData(null);
								EplDataStorage.setTodayMatchScheduleCrawlingTime(null);
							}
						} else { // EplDataStorage VO 변수에 데이터가 들어 있다면...
								// 크롤링된 시간을 변수에 담고...
								Date oldTodayMatchScheduleCrawlingTime = EplDataStorage.getTodayMatchScheduleCrawlingTime();
								// 현재크롤링한시간 변수에 담고...
								Date currentTodayMatchScheduleCrawlingTime = new Date();
								
								// 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
								long difTimes = (currentTodayMatchScheduleCrawlingTime.getTime() - oldTodayMatchScheduleCrawlingTime.getTime()) / 1000; // 초
								
								// 크롤링 할 
								if(difTimes > crawlingDataTimes) {
									ArrayList<HashMap<String, String>> data = tmsc.todayMatchScheduleCrawling();
									if(data != null && data.size() > 0) {
										EplDataStorage.setTodayMatchScheduleData(data);
										EplDataStorage.setTodayMatchScheduleCrawlingTime(new Date());
									}
								}
						}
					} 
					
					// 프리미어리그 크롤링 시작. 
					else if(crawlingDataName.equals("todayEplMatchSchedule")) {
						// DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
						if(EplDataStorage.getTodayEplMatchData() == null) {
							// data에 크롤링하여 데이터를 넣고...
							ArrayList<HashMap<String, String>> data = emsc.eplMatchScheduleCrawling();
							// 만약 크롤링한 데이터가 있으면...
							if(data != null && data.size() > 0) {
								// EplDataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
								EplDataStorage.setTodayEplMatchData(data);
								EplDataStorage.setTodayEplMatchCrawlingTime(new Date());
							} else {
								EplDataStorage.setTodayEplMatchData(null);
								EplDataStorage.setTodayEplMatchCrawlingTime(null);
							}
						} else { // EplDataStorage VO 변수에 데이터가 들어 있다면...
								// 크롤링된 시간을 변수에 담고...
								Date oldTodayEplMatchCrawlingTime = EplDataStorage.getTodayEplMatchCrawlingTime();
								// 현재크롤링한시간 변수에 담고...
								Date currentTodayEplMatchCrawlingTime = new Date();
								
								// 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
								long difTimes = (currentTodayEplMatchCrawlingTime.getTime() - oldTodayEplMatchCrawlingTime.getTime()) / 1000; // 초
								
								// 크롤링 할 
								if(difTimes > crawlingDataTimes) {
									ArrayList<HashMap<String, String>> data = emsc.eplMatchScheduleCrawling();
									if(data != null && data.size() > 0) {
										EplDataStorage.setTodayEplMatchData(data);
										EplDataStorage.setTodayEplMatchCrawlingTime(new Date());
									}
								}
						}
					} else if(crawlingDataName.equals("eplTeamRank")) {
						// DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
						if(EplDataStorage.getEplTeamRankData() == null) {
							// data에 크롤링하여 데이터를 넣고...
							ArrayList<HashMap<String, String>> data = etrc.eplTeamRankCrawling();
							// 만약 크롤링한 데이터가 있으면...
							if(data != null && data.size() > 0) {
								// EplDataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
								EplDataStorage.setEplTeamRankData(data);
								EplDataStorage.setEplTeamRankCrawlingTime(new Date());
							} else {
								EplDataStorage.setEplTeamRankData(null);
								EplDataStorage.setEplTeamRankCrawlingTime(null);
							}
						} else { // EplDataStorage VO 변수에 데이터가 들어 있다면...
								// 크롤링된 시간을 변수에 담고...
								Date oldEplTeamRankCrawlingTime = EplDataStorage.getEplTeamRankCrawlingTime();
								// 현재크롤링한시간 변수에 담고...
								Date currentEplTeamRankCrawlingTime = new Date();
								
								// 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
								long difTimes = (currentEplTeamRankCrawlingTime.getTime() - oldEplTeamRankCrawlingTime.getTime()) / 1000; // 초
								
								// 크롤링 할 
								if(difTimes > crawlingDataTimes) {
									ArrayList<HashMap<String, String>> data = etrc.eplTeamRankCrawling();
									if(data != null && data.size() > 0) {
										EplDataStorage.setEplTeamRankData(data);
										EplDataStorage.setEplTeamRankCrawlingTime(new Date());
									}
								}
						}
					} else if(crawlingDataName.equals("eplPlayerRank")) {
						// DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
						if(EplDataStorage.getEplPlayerRankData() == null) {
							// data에 크롤링하여 데이터를 넣고...
							ArrayList<HashMap<String, String>> data = eprc.eplPlayerRank();
							// 만약 크롤링한 데이터가 있으면...
							if(data != null && data.size() > 0) {
								// EplDataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
								EplDataStorage.setEplPlayerRankData(data);
								EplDataStorage.setEplPlayerRankCrawlingTime(new Date());
							} else {
								EplDataStorage.setEplPlayerRankData(null);
								EplDataStorage.setEplPlayerRankCrawlingTime(null);
							}
						} else { // EplDataStorage VO 변수에 데이터가 들어 있다면...
								// 크롤링된 시간을 변수에 담고...
								Date oldEplPlayerRankCrawlingTime = EplDataStorage.getEplPlayerRankCrawlingTime();
								// 현재크롤링한시간 변수에 담고...
								Date currentEplPlayerRankCrawlingTime = new Date();
								
								// 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
								long difTimes = (currentEplPlayerRankCrawlingTime.getTime() - oldEplPlayerRankCrawlingTime.getTime()) / 1000; // 초
								
								// 크롤링 할 
								if(difTimes > crawlingDataTimes) {
									ArrayList<HashMap<String, String>> data = eprc.eplPlayerRank();
									if(data != null && data.size() > 0) {
										EplDataStorage.setEplPlayerRankData(data);
										EplDataStorage.setEplPlayerRankCrawlingTime(new Date());
									}
								}
						}
					} else if(crawlingDataName.equals("eplPlayerAsistRank")) {
		                  // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
		                  if(EplDataStorage.getEplPlayerAsistRankData() == null) {
		                     // data에 크롤링하여 데이터를 넣고...
		                     ArrayList<HashMap<String, String>> data = eparc.eplPlayerAsistRank();
		                     // 만약 크롤링한 데이터가 있으면...
		                     if(data != null && data.size() > 0) {
		                        // EplDataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
		                        EplDataStorage.setEplPlayerAsistRankData(data);
		                        EplDataStorage.setEplPlayerAsistRankCrawlingTime(new Date());
		                     }
		                  } else { // EplDataStorage VO 변수에 데이터가 들어 있다면...
		                        // 크롤링된 시간을 변수에 담고...
		                        Date oldEplPlayerAsistRankCrawlingTime = EplDataStorage.getEplPlayerAsistRankCrawlingTime();
		                        // 현재크롤링한시간 변수에 담고...
		                        Date currentEplPlayerAsistRankCrawlingTime = new Date();
		                        
		                        // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
		                        long difTimes = (currentEplPlayerAsistRankCrawlingTime.getTime() - oldEplPlayerAsistRankCrawlingTime.getTime()) / 1000; // 초
		                        
		                        // 크롤링 할 
		                        if(difTimes > crawlingDataTimes) {
		                           ArrayList<HashMap<String, String>> data = eparc.eplPlayerAsistRank();
		                           if(data != null && data.size() > 0) {
		                              EplDataStorage.setEplPlayerAsistRankData(data);
		                              EplDataStorage.setEplPlayerAsistRankCrawlingTime(new Date());
		                           }
		                        }
		                  }
		               } // 프리미어리그 크롤링 종료. 
					
					
					
					// 라리가 크롤링 시작.
					else if(crawlingDataName.equals("todayLaligaMatchSchedule")) {
	                     // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                     if(LaligaDataStorage.getTodayLaligaMatchData() == null) {
	                        // data에 크롤링하여 데이터를 넣고...
	                        ArrayList<HashMap<String, String>> data = lmsc.laligaMatchScheduleCrawling();
	                        // 만약 크롤링한 데이터가 있으면...
	                        if(data != null && data.size() > 0) {
	                           // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                           LaligaDataStorage.setTodayLaligaMatchData(data);
	                           LaligaDataStorage.setTodayLaligaMatchCrawlingTime(new Date());
	                        } else {
	                           LaligaDataStorage.setTodayLaligaMatchData(null);
	                           LaligaDataStorage.setTodayLaligaMatchCrawlingTime(null);
	                        }
	                     } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                           // 크롤링된 시간을 변수에 담고...
	                           Date oldTodayLaligaMatchCrawlingTime = LaligaDataStorage.getTodayLaligaMatchCrawlingTime();
	                           // 현재크롤링한시간 변수에 담고...
	                           Date currentTodayLaligaMatchCrawlingTime = new Date();
	                           
	                           // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                           long difTimes = (currentTodayLaligaMatchCrawlingTime.getTime() - oldTodayLaligaMatchCrawlingTime.getTime()) / 1000; // 초
	                           
	                           // 크롤링 할 
	                           if(difTimes > crawlingDataTimes) {
	                              ArrayList<HashMap<String, String>> data = lmsc.laligaMatchScheduleCrawling();
	                              if(data != null && data.size() > 0) {
	                                 LaligaDataStorage.setTodayLaligaMatchData(data);
	                                 LaligaDataStorage.setTodayLaligaMatchCrawlingTime(new Date());
	                              }
	                           }
	                     }
	                  } else if(crawlingDataName.equals("laligaTeamRank")) {
	                     // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                     if(LaligaDataStorage.getLaligaTeamRankData() == null) {
	                        // data에 크롤링하여 데이터를 넣고...
	                        ArrayList<HashMap<String, String>> data = ltrc.laligaTeamRankCrawling();
	                        // 만약 크롤링한 데이터가 있으면...
	                        if(data != null && data.size() > 0) {
	                           // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                           LaligaDataStorage.setLaligaTeamRankData(data);
	                           LaligaDataStorage.setLaligaTeamRankCrawlingTime(new Date());
	                        } else {
	                           LaligaDataStorage.setLaligaTeamRankData(null);
	                           LaligaDataStorage.setLaligaTeamRankCrawlingTime(null);
	                        }
	                     } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                           // 크롤링된 시간을 변수에 담고...
	                           Date oldLaligaTeamRankCrawlingTime = LaligaDataStorage.getLaligaTeamRankCrawlingTime();
	                           // 현재크롤링한시간 변수에 담고...
	                           Date currentLaligaTeamRankCrawlingTime = new Date();
	                           
	                           // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                           long difTimes = (currentLaligaTeamRankCrawlingTime.getTime() - oldLaligaTeamRankCrawlingTime.getTime()) / 1000; // 초
	                           
	                           // 크롤링 할 
	                           if(difTimes > crawlingDataTimes) {
	                              ArrayList<HashMap<String, String>> data = ltrc.laligaTeamRankCrawling();
	                              if(data != null && data.size() > 0) {
	                                 LaligaDataStorage.setLaligaTeamRankData(data);
	                                 LaligaDataStorage.setLaligaTeamRankCrawlingTime(new Date());
	                              }
	                           }
	                     }
	                  } else if(crawlingDataName.equals("laligaPlayerRank")) {
	                     // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                     if(LaligaDataStorage.getLaligaPlayerRankData() == null) {
	                        // data에 크롤링하여 데이터를 넣고...
	                        ArrayList<HashMap<String, String>> data = lprc.laligaPlayerRank();
	                        // 만약 크롤링한 데이터가 있으면...
	                        if(data != null && data.size() > 0) {
	                           // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                           LaligaDataStorage.setLaligaPlayerRankData(data);
	                           LaligaDataStorage.setLaligaPlayerRankCrawlingTime(new Date());
	                        } else {
	                           LaligaDataStorage.setLaligaPlayerRankData(null);
	                           LaligaDataStorage.setLaligaPlayerRankCrawlingTime(null);
	                        }
	                     } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                           // 크롤링된 시간을 변수에 담고...
	                           Date oldLaligaPlayerRankCrawlingTime = LaligaDataStorage.getLaligaPlayerRankCrawlingTime();
	                           // 현재크롤링한시간 변수에 담고...
	                           Date currentLaligaPlayerRankCrawlingTime = new Date();
	                           
	                           // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                           long difTimes = (currentLaligaPlayerRankCrawlingTime.getTime() - oldLaligaPlayerRankCrawlingTime.getTime()) / 1000; // 초
	                           
	                           // 크롤링 할 
	                           if(difTimes > crawlingDataTimes) {
	                              ArrayList<HashMap<String, String>> data = lprc.laligaPlayerRank();
	                              if(data != null && data.size() > 0) {
	                                 LaligaDataStorage.setLaligaPlayerRankData(data);
	                                 LaligaDataStorage.setLaligaPlayerRankCrawlingTime(new Date());
	                              }
	                           }
	                     }
	                  } else if(crawlingDataName.equals("laligaPlayerAsistRank")) {
	                           // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                           if(LaligaDataStorage.getLaligaPlayerAsistRankData() == null) {
	                              // data에 크롤링하여 데이터를 넣고...
	                              ArrayList<HashMap<String, String>> data = lparc.laligaPlayerAsistRank();
	                              // 만약 크롤링한 데이터가 있으면...
	                              if(data != null && data.size() > 0) {
	                                 // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                 LaligaDataStorage.setLaligaPlayerAsistRankData(data);
	                                 LaligaDataStorage.setLaligaPlayerAsistRankCrawlingTime(new Date());
	                              }
	                           } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                 // 크롤링된 시간을 변수에 담고...
	                                 Date oldLaligaPlayerAsistRankCrawlingTime = LaligaDataStorage.getLaligaPlayerAsistRankCrawlingTime();
	                                 // 현재크롤링한시간 변수에 담고...
	                                 Date currentLaligaPlayerAsistRankCrawlingTime = new Date();
	                                 
	                                 // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                 long difTimes = (currentLaligaPlayerAsistRankCrawlingTime.getTime() - oldLaligaPlayerAsistRankCrawlingTime.getTime()) / 1000; // 초
	                                 
	                                 // 크롤링 할 
	                                 if(difTimes > crawlingDataTimes) {
	                                    ArrayList<HashMap<String, String>> data = lparc.laligaPlayerAsistRank();
	                                    if(data != null && data.size() > 0) {
	                                       LaligaDataStorage.setLaligaPlayerAsistRankData(data);
	                                       LaligaDataStorage.setLaligaPlayerAsistRankCrawlingTime(new Date());
	                                    }
	                                 }
	                           }
	                        }
					// 라리가 크롤링 종료.
					
					// 분데스리가 크롤링 시작.
	                  else if(crawlingDataName.equals("todayBundesligaMatchSchedule")) {
	                           // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                           if(BundesligaDataStorage.getTodayBundesligaMatchData() == null) {
	                              // data에 크롤링하여 데이터를 넣고...
	                              ArrayList<HashMap<String, String>> data = bmsc.bundesligaMatchScheduleCrawling();
	                              // 만약 크롤링한 데이터가 있으면...
	                              if(data != null && data.size() > 0) {
	                                 // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                 BundesligaDataStorage.setTodayBundesligaMatchData(data);
	                                 BundesligaDataStorage.setTodayBundesligaMatchCrawlingTime(new Date());
	                              } else {
	                                 BundesligaDataStorage.setTodayBundesligaMatchData(null);
	                                 BundesligaDataStorage.setTodayBundesligaMatchCrawlingTime(null);
	                              }
	                           } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                 // 크롤링된 시간을 변수에 담고...
	                                 Date oldTodayBundesligaMatchCrawlingTime = BundesligaDataStorage.getTodayBundesligaMatchCrawlingTime();
	                                 // 현재크롤링한시간 변수에 담고...
	                                 Date currentTodayBundesligaMatchCrawlingTime = new Date();
	                                 
	                                 // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                 long difTimes = (currentTodayBundesligaMatchCrawlingTime.getTime() - oldTodayBundesligaMatchCrawlingTime.getTime()) / 1000; // 초
	                                 
	                                 // 크롤링 할 
	                                 if(difTimes > crawlingDataTimes) {
	                                    ArrayList<HashMap<String, String>> data = bmsc.bundesligaMatchScheduleCrawling();
	                                    if(data != null && data.size() > 0) {
	                                       BundesligaDataStorage.setTodayBundesligaMatchData(null);
	                                      BundesligaDataStorage.setTodayBundesligaMatchCrawlingTime(null);
	                                    }
	                                 }
	                           }
	                        } else if(crawlingDataName.equals("bundesligaTeamRank")) {
	                           // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                           if(BundesligaDataStorage.getBundesligaTeamRankData() == null) {
	                              // data에 크롤링하여 데이터를 넣고...
	                              ArrayList<HashMap<String, String>> data = btrc.bundesligaTeamRankCrawling();
	                              // 만약 크롤링한 데이터가 있으면...
	                              if(data != null && data.size() > 0) {
	                                 // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                 BundesligaDataStorage.setBundesligaTeamRankData(data);
	                                 BundesligaDataStorage.setBundesligaTeamRankCrawlingTime(new Date());
	                              } else {
	                                 BundesligaDataStorage.setBundesligaTeamRankData(null);
	                                 BundesligaDataStorage.setBundesligaTeamRankCrawlingTime(null);
	                              }
	                           } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                 // 크롤링된 시간을 변수에 담고...
	                                 Date oldBundesligaTeamRankCrawlingTime = BundesligaDataStorage.getBundesligaTeamRankCrawlingTime();
	                                 // 현재크롤링한시간 변수에 담고...
	                                 Date currentBundesligaTeamRankCrawlingTime = new Date();
	                                 
	                                 // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                 long difTimes = (currentBundesligaTeamRankCrawlingTime.getTime() - oldBundesligaTeamRankCrawlingTime.getTime()) / 1000; // 초
	                                 
	                                 // 크롤링 할 
	                                 if(difTimes > crawlingDataTimes) {
	                                    ArrayList<HashMap<String, String>> data = btrc.bundesligaTeamRankCrawling();
	                                    if(data != null && data.size() > 0) {
	                                       BundesligaDataStorage.setBundesligaTeamRankData(data);
	                                      BundesligaDataStorage.setBundesligaTeamRankCrawlingTime(new Date());
	                                    }
	                                 }
	                           }
	                        } else if(crawlingDataName.equals("bundesligaPlayerRank")) {
	                           // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                           if(BundesligaDataStorage.getBundesligaPlayerRankData() == null) {
	                              // data에 크롤링하여 데이터를 넣고...
	                              ArrayList<HashMap<String, String>> data = bprc.bundesligaPlayerRank();
	                              // 만약 크롤링한 데이터가 있으면...
	                              if(data != null && data.size() > 0) {
	                                 // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                 BundesligaDataStorage.setBundesligaPlayerRankData(data);
	                                 BundesligaDataStorage.setBundesligaPlayerRankCrawlingTime(new Date());
	                              } else {
	                                 BundesligaDataStorage.setBundesligaPlayerRankData(null);
	                                 BundesligaDataStorage.setBundesligaPlayerRankCrawlingTime(null);
	                              }
	                           } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                 // 크롤링된 시간을 변수에 담고...
	                                 Date oldBundesligaPlayerRankCrawlingTime = BundesligaDataStorage.getBundesligaPlayerRankCrawlingTime();
	                                 // 현재크롤링한시간 변수에 담고...
	                                 Date currentBundesligaPlayerRankCrawlingTime = new Date();
	                                 
	                                 // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                 long difTimes = (currentBundesligaPlayerRankCrawlingTime.getTime() - oldBundesligaPlayerRankCrawlingTime.getTime()) / 1000; // 초
	                                 
	                                 // 크롤링 할 
	                                 if(difTimes > crawlingDataTimes) {
	                                    ArrayList<HashMap<String, String>> data = bprc.bundesligaPlayerRank();
	                                    if(data != null && data.size() > 0) {
	                                       BundesligaDataStorage.setBundesligaPlayerRankData(data);
	                                      BundesligaDataStorage.setBundesligaPlayerRankCrawlingTime(new Date());
	                                    }
	                                 }
	                           }
	                        } else if(crawlingDataName.equals("bundesligaPlayerAsistRank")) {
	                                 // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                                 if(BundesligaDataStorage.getBundesligaPlayerAsistRankData() == null) {
	                                    // data에 크롤링하여 데이터를 넣고...
	                                    ArrayList<HashMap<String, String>> data = bparc.bundesligaPlayerAsistRank();
	                                    // 만약 크롤링한 데이터가 있으면...
	                                    if(data != null && data.size() > 0) {
	                                       // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                       BundesligaDataStorage.setBundesligaPlayerAsistRankData(data);
	                                       BundesligaDataStorage.setBundesligaPlayerAsistRankCrawlingTime(new Date());
	                                    } else {
	                                       BundesligaDataStorage.setBundesligaPlayerAsistRankData(null);
	                                       BundesligaDataStorage.setBundesligaPlayerAsistRankCrawlingTime(null);
	                                    }
	                                 } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                       // 크롤링된 시간을 변수에 담고...
	                                       Date oldBundesligaPlayerAsistRankCrawlingTime = BundesligaDataStorage.getBundesligaPlayerAsistRankCrawlingTime();
	                                       // 현재크롤링한시간 변수에 담고...
	                                       Date currentBundesligaPlayerAsistRankCrawlingTime = new Date();
	                                       
	                                       // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                       long difTimes = (currentBundesligaPlayerAsistRankCrawlingTime.getTime() - oldBundesligaPlayerAsistRankCrawlingTime.getTime()) / 1000; // 초
	                                       
	                                       // 크롤링 할 
	                                       if(difTimes > crawlingDataTimes) {
	                                          ArrayList<HashMap<String, String>> data = bparc.bundesligaPlayerAsistRank();
	                                          if(data != null && data.size() > 0) {
	                                             BundesligaDataStorage.setBundesligaPlayerAsistRankData(data);
	                                           BundesligaDataStorage.setBundesligaPlayerAsistRankCrawlingTime(new Date());
	                                          }
	                                       }
	                                 }
	                              }
	                  // 분데스리가 크롤링 종료.
					
					// 세리에A 크롤링 시작.
	                        else if(crawlingDataName.equals("todaySerieaMatchSchedule")) {
	                                 // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                                 if(SerieaDataStorage.getTodaySerieaMatchData() == null) {
	                                    // data에 크롤링하여 데이터를 넣고...
	                                    ArrayList<HashMap<String, String>> data = smsc.serieaMatchScheduleCrawling();
	                                    // 만약 크롤링한 데이터가 있으면...
	                                    if(data != null && data.size() > 0) {
	                                       // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                       SerieaDataStorage.setTodaySerieaMatchData(data);
	                                       SerieaDataStorage.setTodaySerieaMatchCrawlingTime(new Date());
	                                    } else {
	                                       SerieaDataStorage.setTodaySerieaMatchData(null);
	                                       SerieaDataStorage.setTodaySerieaMatchCrawlingTime(null);
	                                    }
	                                 } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                       // 크롤링된 시간을 변수에 담고...
	                                       Date oldTodaySerieaMatchCrawlingTime = SerieaDataStorage.getTodaySerieaMatchCrawlingTime();
	                                       // 현재크롤링한시간 변수에 담고...
	                                       Date currentTodaySerieaMatchCrawlingTime = new Date();
	                                       
	                                       // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                       long difTimes = (currentTodaySerieaMatchCrawlingTime.getTime() - oldTodaySerieaMatchCrawlingTime.getTime()) / 1000; // 초
	                                       
	                                       // 크롤링 할 
	                                       if(difTimes > crawlingDataTimes) {
	                                          ArrayList<HashMap<String, String>> data = smsc.serieaMatchScheduleCrawling();
	                                          if(data != null && data.size() > 0) {
	                                             SerieaDataStorage.setTodaySerieaMatchData(data);
	                                            SerieaDataStorage.setTodaySerieaMatchCrawlingTime(new Date());
	                                          }
	                                       }
	                                 }
	                              } else if(crawlingDataName.equals("serieaTeamRank")) {
	                                 // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                                 if(SerieaDataStorage.getSerieaTeamRankData() == null) {
	                                    // data에 크롤링하여 데이터를 넣고...
	                                    ArrayList<HashMap<String, String>> data = strc.serieaTeamRankCrawling();
	                                    // 만약 크롤링한 데이터가 있으면...
	                                    if(data != null && data.size() > 0) {
	                                       // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                       SerieaDataStorage.setSerieaTeamRankData(data);
	                                       SerieaDataStorage.setSerieaTeamRankCrawlingTime(new Date());
	                                    } else {
	                                       SerieaDataStorage.setSerieaTeamRankData(null);
	                                       SerieaDataStorage.setSerieaTeamRankCrawlingTime(null);
	                                    }
	                                 } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                       // 크롤링된 시간을 변수에 담고...
	                                       Date oldSerieaTeamRankCrawlingTime = SerieaDataStorage.getSerieaTeamRankCrawlingTime();
	                                       // 현재크롤링한시간 변수에 담고...
	                                       Date currentSerieaTeamRankCrawlingTime = new Date();
	                                       
	                                       // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                       long difTimes = (currentSerieaTeamRankCrawlingTime.getTime() - oldSerieaTeamRankCrawlingTime.getTime()) / 1000; // 초
	                                       
	                                       // 크롤링 할 
	                                       if(difTimes > crawlingDataTimes) {
	                                          ArrayList<HashMap<String, String>> data = strc.serieaTeamRankCrawling();
	                                          if(data != null && data.size() > 0) {
	                                             SerieaDataStorage.setSerieaTeamRankData(data);
	                                            SerieaDataStorage.setSerieaTeamRankCrawlingTime(new Date());
	                                          }
	                                       }
	                                 }
	                              } else if(crawlingDataName.equals("serieaPlayerRank")) {
	                                 // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                                 if(SerieaDataStorage.getSerieaPlayerRankData() == null) {
	                                    // data에 크롤링하여 데이터를 넣고...
	                                    ArrayList<HashMap<String, String>> data = sprc.serieaPlayerRank();
	                                    // 만약 크롤링한 데이터가 있으면...
	                                    if(data != null && data.size() > 0) {
	                                       // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                       SerieaDataStorage.setSerieaPlayerRankData(data);
	                                       SerieaDataStorage.setSerieaPlayerRankCrawlingTime(new Date());
	                                    } else {
	                                       SerieaDataStorage.setSerieaPlayerRankData(null);
	                                       SerieaDataStorage.setSerieaPlayerRankCrawlingTime(null);
	                                    }
	                                 } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                       // 크롤링된 시간을 변수에 담고...
	                                       Date oldSerieaPlayerRankCrawlingTime = SerieaDataStorage.getSerieaPlayerRankCrawlingTime();
	                                       // 현재크롤링한시간 변수에 담고...
	                                       Date currentSerieaPlayerRankCrawlingTime = new Date();
	                                       
	                                       // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                       long difTimes = (currentSerieaPlayerRankCrawlingTime.getTime() - oldSerieaPlayerRankCrawlingTime.getTime()) / 1000; // 초
	                                       
	                                       // 크롤링 할 
	                                       if(difTimes > crawlingDataTimes) {
	                                          ArrayList<HashMap<String, String>> data = sprc.serieaPlayerRank();
	                                          if(data != null && data.size() > 0) {
	                                             SerieaDataStorage.setSerieaPlayerRankData(data);
	                                            SerieaDataStorage.setSerieaPlayerRankCrawlingTime(new Date());
	                                          }
	                                       }
	                                 }
	                              } else if(crawlingDataName.equals("serieaPlayerAsistRank")) {
	                                       // DataSotrage VO 의 변수에 데이터가 들어있지 않다면...
	                                       if(SerieaDataStorage.getSerieaPlayerAsistRankData() == null) {
	                                          // data에 크롤링하여 데이터를 넣고...
	                                          ArrayList<HashMap<String, String>> data = sparc.serieaPlayerAsistRank();
	                                          // 만약 크롤링한 데이터가 있으면...
	                                          if(data != null && data.size() > 0) {
	                                             // DataStorage VO 안의 변수에 크롤링한 data와 크롤링한 시간을 담아라...
	                                             SerieaDataStorage.setSerieaPlayerAsistRankData(data);
	                                             SerieaDataStorage.setSerieaPlayerAsistRankCrawlingTime(new Date());
	                                          } else {
	                                             SerieaDataStorage.setSerieaPlayerAsistRankData(null);
	                                             SerieaDataStorage.setSerieaPlayerAsistRankCrawlingTime(null);
	                                          }
	                                       } else { // DataStorage VO 변수에 데이터가 들어 있다면...
	                                             // 크롤링된 시간을 변수에 담고...
	                                             Date oldSerieaPlayerAsistRankCrawlingTime = SerieaDataStorage.getSerieaPlayerAsistRankCrawlingTime();
	                                             // 현재크롤링한시간 변수에 담고...
	                                             Date currentSerieaPlayerAsistRankCrawlingTime = new Date();
	                                             
	                                             // 현재 크롤링시간과 최근 크롤링시간의 차이를 계산하여 변수에 저장...
	                                             long difTimes = (currentSerieaPlayerAsistRankCrawlingTime.getTime() - oldSerieaPlayerAsistRankCrawlingTime.getTime()) / 1000; // 초
	                                             
	                                             // 크롤링 할 
	                                             if(difTimes > crawlingDataTimes) {
	                                                ArrayList<HashMap<String, String>> data = sparc.serieaPlayerAsistRank();
	                                                if(data != null && data.size() > 0) {
	                                                   SerieaDataStorage.setSerieaPlayerAsistRankData(data);
	                                                 SerieaDataStorage.setSerieaPlayerAsistRankCrawlingTime(new Date());
	                                                }
	                                             }
	                                       }
	                                    }
	                        // 세리에A 크롤링 종료.
               
					
					
				} // for finish 
				
				
				Thread.sleep(1000*60*50);
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
		}
	}
	
	
	
	
	}
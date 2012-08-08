package com.leonty.calculation;

import java.util.Properties;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.leonty.etm.rule.DayLimits;
import com.leonty.etm.rule.WeekLimits;
import com.leonty.etm.time.WorkDay;
import com.leonty.etm.time.WorkWeek;

public class Overtime {

	private KnowledgeBase dayKnowledgeBase;
	private KnowledgeBase weekKnowledgeBase;
	
	public Overtime() {
		
		dayKnowledgeBase = createKnowledgeBase("CaliforniaLawDayRules.drl");
		weekKnowledgeBase = createKnowledgeBase("CaliforniaLawWeekRules.drl");
	}
	
	public WorkDay calculateDay(WorkDay workDay, DayLimits dayLimits) {

		StatefulKnowledgeSession session = dayKnowledgeBase.newStatefulKnowledgeSession(); 
		
		try {
			
			session.insert(dayLimits);
			session.insert(workDay);
			
			session.fireAllRules();
			
		} finally {
			session.dispose();
		}
		
		return workDay;
	}
	
	public WorkWeek calculateWeek(WorkWeek workWeek, WeekLimits weekLimits, DayLimits dayLimits) {

		StatefulKnowledgeSession session = weekKnowledgeBase.newStatefulKnowledgeSession(); 		
		
		try {
			
			session.insert(weekLimits);
			session.insert(dayLimits);
			session.insert(workWeek);
			
			session.fireAllRules();
			
		} finally {
			session.dispose();
		}
		
		
		return workWeek;
	}
	
	private KnowledgeBase createKnowledgeBase(String drlFile) {
		Properties properties = new Properties();  
		properties.setProperty( "drools.dialect.java.compiler","JANINO" );  
		
		KnowledgeBuilderConfiguration config = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(properties);
		
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder(config);  
        
        //Add drl file into builder  
        builder.add(ResourceFactory.newInputStreamResource(getClass().getResourceAsStream("/" + drlFile)), ResourceType.DRL);  
        
        if (builder.hasErrors()) {  
            throw new RuntimeException(builder.getErrors().toString());  
        }  
  
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();  
  
        //Add to Knowledge Base packages from the builder which are actually the rules from the drl file.  
        knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());  
  
        return knowledgeBase;		
	}
	
}

package org.primaldev.ppm.ui.main;

import java.util.Calendar;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.primaldev.ppm.util.ChartTypeComponent;
import org.primaldev.ppm.util.ChartTypeGenerator;
import org.primaldev.ppm.util.ProcessUtil;
import org.primaldev.ppm.util.ReportUtil;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.shared.ui.Connect;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;


@Connect(org.dussan.vaadin.dcharts.DCharts.class)
public class WelcomeSummaryUI extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private AbsoluteLayout taskProcessLayout;
	@AutoGenerated
	private Button refreshButton;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	
	public WelcomeSummaryUI() {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		setSummary();
		setStyle();
		addClickListeners();
	}

	private void setStyle(){
		
	}
	
	@SuppressWarnings("serial")
	private void addClickListeners(){
		refreshButton.addClickListener(new Button.ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				setSummary();
				
			}
		});
		
	}
	
	
	
	protected List<HistoricTaskInstance> queryForTasksToShow() {		
		 HistoricTaskInstanceQuery query = ProcessUtil.getHistoryService().createHistoricTaskInstanceQuery();
		 query.taskCompletedBefore(getEndOfDay().getTime()).orderByTaskPriority().desc().orderByTaskDueDate().desc();
		 return query.list();
	}
	
	private void setSummary(){
	
		/*
		 List<HistoricTaskInstance> completed = queryForTasksToShow();		
		 ChartTypeComponent charta;
		 
		for (HistoricTaskInstance procInst : completed){
			ReportUtil.generateTaskDurationReport(procInst.getProcessDefinitionId());			
			charta = generateReport(procInst.getId());			
			taskChartTabSheet.addTab(charta, procInst.getId());			
		}
		*/
				
		ChartTypeComponent chart = generateReport(ReportUtil.getProcessListDuration().getId());
		
		if (chart !=null) {
			taskProcessLayout.removeAllComponents();
			taskProcessLayout.addComponent(chart);
			
		}
		
		
	}


	private Calendar getEndOfDay(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE,59);
		return cal;
	}

	
	protected ChartTypeComponent generateReport(String processInstanceId) {
		// Report dataset is stored as historical variable as json
		HistoricVariableInstance historicVariableInstance = ProcessUtil.getHistoryService()		
		.createHistoricVariableInstanceQuery()
		.processInstanceId(processInstanceId)
		.variableName("reportData")
		.singleResult();
	
		if(historicVariableInstance != null) {
			// Generate chart
			byte[] reportData = (byte[]) historicVariableInstance.getValue();
			ChartTypeComponent chart = ChartTypeGenerator.generateChart(reportData);
			chart.setWidth(100, Unit.PERCENTAGE);
			chart.setHeight(100, Unit.PERCENTAGE);
		// The historic process instance can now be removed from the system
		// Only when save is clicked, the report will be regenerated
		
			ProcessEngines.getDefaultProcessEngine().getHistoryService().deleteHistoricProcessInstance(processInstanceId);
			return chart;
	
		}
		return null;
		
		
		
	}
	
	
	
	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// refreshButton
		refreshButton = new Button();
		refreshButton.setCaption("Refresh");
		refreshButton.setImmediate(true);
		refreshButton.setWidth("-1px");
		refreshButton.setHeight("-1px");
		mainLayout.addComponent(refreshButton, "top:54.0px;left:60.0px;");
		
		// taskProcessLayout
		taskProcessLayout = new AbsoluteLayout();
		taskProcessLayout.setImmediate(false);
		taskProcessLayout.setWidth("560px");
		taskProcessLayout.setHeight("360px");
		mainLayout.addComponent(taskProcessLayout, "top:80.0px;left:60.0px;");
		
		return mainLayout;
	}

}

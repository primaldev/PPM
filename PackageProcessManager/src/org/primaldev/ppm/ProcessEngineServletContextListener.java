package org.primaldev.ppm;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;

@WebListener
public class ProcessEngineServletContextListener implements
		ServletContextListener {

	private static final Logger log = Logger
			.getLogger(ProcessEngineServletContextListener.class.getName());

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.info("Destroying process engines");
		ProcessEngines.destroy();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("Initializing process engines");
		ProcessEngines.init();
		createGroupsIfNotPresent();
		createAdminUserIfNotPresent();
		deployProcesses();
	}

	private void createAdminUserIfNotPresent() {
		if (!isAdminUserPresent()) {
			createAdminUser();
		}
	}

	private void createGroupsIfNotPresent() {
		if (!isGroupPresent("managers")) {
			createGroup("managers", "Managers", "process");
		}
		if (!isGroupPresent("developers")) {
			createGroup("developers", "Developers", "process" );
		}
		if (!isGroupPresent("reporters")) {
			createGroup("reporters", "Reporters", "process");
		}
		if (!isGroupPresent("siteadmin")) {
			createGroup("siteadmin", "SiteAdmin", "system");
		}
		if (!isGroupPresent("poweruser")) {
			createGroup("poweruser", "PowerUser", "system");
		}
		
		
	}

	private boolean isAdminUserPresent() {
		UserQuery query = getIdentityService().createUserQuery();
		query.userId("admin");
		return query.count() > 0;
	}

	private void createAdminUser() {
		log.info("Creating an administration user with the username 'admin' and password 'password'");
		User adminUser = getIdentityService().newUser("admin");
		adminUser.setFirstName("Arnold");
		adminUser.setLastName("Administrator");
		adminUser.setPassword("password");
		getIdentityService().saveUser(adminUser);
		assignAdminUserToGroups();
	}

	private void assignAdminUserToGroups() {
		getIdentityService().createMembership("admin", "managers");
		getIdentityService().createMembership("admin", "developers");
		getIdentityService().createMembership("admin", "reporters");
		getIdentityService().createMembership("admin", "siteadmin");
	}

	private boolean isGroupPresent(String groupId) {
		GroupQuery query = getIdentityService().createGroupQuery();
		query.groupId(groupId);
		return query.count() > 0;
	}

	private void createGroup(String groupId, String groupName, String groupType) {
		log.log(Level.INFO,
				"Creating a group with the id '{1}' and name '{2}'",
				new Object[] { groupId, groupName });
		Group group = getIdentityService().newGroup(groupId);
		group.setName(groupName);
		group.setType(groupType);
		getIdentityService().saveGroup(group);
	}

	private IdentityService getIdentityService() {
		return ProcessEngines.getDefaultProcessEngine().getIdentityService();
	}

	private void deployProcesses() {
		log.info("Deploying processes");
		RepositoryService repositoryService = ProcessEngines
				.getDefaultProcessEngine().getRepositoryService();
		repositoryService
				.createDeployment()
				.addClasspathResource(
						"org/primaldev/ppm/bpmn/QuickProcess.bpmn")
				.deploy();
	}

}

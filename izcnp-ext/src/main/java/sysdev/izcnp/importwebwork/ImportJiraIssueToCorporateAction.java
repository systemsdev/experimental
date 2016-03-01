package sysdev.izcnp.importwebwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.comments.CommentManager;
import com.atlassian.jira.issue.comments.Comment;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.plugin.webresource.WebResourceManager;

public class ImportJiraIssueToCorporateAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(ImportJiraIssueToCorporateAction.class);
    private final IssueManager _issueManager;
    private final CommentManager _commentManager;
    private final WebResourceManager _webResourceManager;
    
    public ImportJiraIssueToCorporateAction() { 
        this._issueManager = ComponentAccessor.getIssueManager();
        this._commentManager = ComponentAccessor.getCommentManager();
        this._webResourceManager = ComponentAccessor.getWebResourceManager(); 
    }
    
    @Override
    public String execute() throws Exception {
        
        log.info("Entered execute() for issue: " + getIssueId());
    
        Issue issue = _issueManager.getIssueObject(getIssueId());
        
        log.info("Found issue: " + issue.toString());
        log.info("Issue key: " + issue.getKey());
        log.info("Summary: " + issue.getSummary());
        log.info("Description: " + issue.getDescription());
        log.info("Reporter: " + issue.getReporter().getName());
        
        List<Comment> comments = _commentManager.getComments(issue);
        
        for(Comment comment : comments) {
            log.debug("Comment:" + comment.getBody() + " by " + comment.getAuthorFullName());
        }  
        
        return super.execute(); //returns SUCCESS
    
        // We want to redirect back to the view issue page so
        //return returnCompleteWithInlineRedirectAndMsg("/browse/" + issue.getKey(), "Issue has been sent to Corporate",
        //                                              JiraWebActionSupport.MessageType.SUCCESS, false, null);
    }
    
    /**
     * Issue id is a local variable that is set from HTTP request parameters.
     */
    private String _issueId = "";

    /**
     * This method is automatically discovered and called by JSP and Webwork
     * if the name matches the id of a parameter passed in an HTML form.
     * The class of the parameter (String) has to match, and the
     * method has to be public or it is silently ignored.
     */
    public void setIssueId(String value) {
        log.debug("Setting issue id to: " + value);
        this._issueId = value;
    }

    /**
     * This is how the local variable is always accessed, since only this
     * action knows that its name isn't really "myfirstparameter".
     */
    public String getIssueId() {
        log.debug("Getting issue id");
        return _issueId;
    }
}

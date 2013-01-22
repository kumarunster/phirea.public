package test.graphiti.nonemf.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import test.graphiti.nonemf.diagram.POJOIndependenceSolver;
import test.graphiti.nonemf.domainmodel.CardinalityType;
import test.graphiti.nonemf.domainmodel.Predicate;
import test.graphiti.nonemf.domainmodel.StatementRule;

public class StatementRulePropertiesDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	protected boolean btnOkPressed = false;
	protected boolean initialized = false;
	protected StatementRulePropertiesDialog thisDialog;
	private StatementRule statementRule;
	private List<String> predicateNames;
	protected Combo comboPredicates;
	protected Combo comboFromCardinality;
	protected Combo comboToCardinality;
	private POJOIndependenceSolver pojoIndependeceSolver; 

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public StatementRulePropertiesDialog(Shell parent, int style) {
		super(parent, SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
		setText("Statement Rule Properties");
		btnOkPressed = false;
		thisDialog = this;
		initialized = false;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		if(!initialized)
			createFakeContexts();
			
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}


	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 189);
		shell.setText(getText());
		shell.setLayout(new GridLayout(2, false));
		
		Label lblFromCardinality = new Label(shell, SWT.NONE);
		lblFromCardinality.setBounds(0, 0, 58, 14);
		lblFromCardinality.setText("From Cardinality");
		
		comboFromCardinality = new Combo(shell, SWT.NONE);
		comboFromCardinality.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboFromCardinality.setItems(CardinalityType.getCardinalities());		
		comboFromCardinality.setText(statementRule.getFromCardinality().getCardinalityName());
		
		Label lblPredicate = new Label(shell, SWT.NONE);
		lblPredicate.setText("Predicate");
		
		comboPredicates = new Combo(shell, SWT.NONE);
		comboPredicates.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		for(String predicate: predicateNames)
			comboPredicates.add(predicate);
		
		comboPredicates.setText(statementRule.getPredicate().getName());
		
		Label lblToCardinality = new Label(shell, SWT.NONE);
		lblToCardinality.setText("To Cardinality");
		
		comboToCardinality = new Combo(shell, SWT.NONE);
		comboToCardinality.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboToCardinality.setItems(CardinalityType.getCardinalities());
		comboToCardinality.setText(statementRule.getToCardinality().getCardinalityName());
		
		Composite composite = new Composite(shell, SWT.NONE);
		RowLayout rl_composite = new RowLayout(SWT.HORIZONTAL);
		rl_composite.justify = true;
		rl_composite.fill = true;
		rl_composite.center = true;
		composite.setLayout(rl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 2, 1));
		
		Button btnOK = new Button(composite, SWT.NONE);
		btnOK.setLayoutData(new RowData(80, SWT.DEFAULT));
		btnOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String selectedName = comboPredicates.getText();
				
				if(!predicateNames.contains(selectedName)) {
					Predicate newPredicate = new Predicate();
					newPredicate.setName(selectedName);
					pojoIndependeceSolver.registerPredicate(newPredicate);
				}
				
				//find Predicate
				Predicate foundPredicate = null;
				for(Predicate predicate : pojoIndependeceSolver.getPredicatesList()) {
					if(predicate.getName().equals(selectedName)) {
						foundPredicate = predicate;
						break;
					}
				}
					
				statementRule.setPredicate(foundPredicate);
				
				String selectedFromCardinality = comboFromCardinality.getText();
				String selectedToCardinality = comboToCardinality.getText();
				
				CardinalityType fromCardinality = CardinalityType.findForName(selectedFromCardinality);
				CardinalityType toCardinality = CardinalityType.findForName(selectedToCardinality);
				
				statementRule.setFromCardinality(fromCardinality);
				statementRule.setToCardinality(toCardinality);
				
				btnOkPressed = true;
				shell.dispose();
			}
		});
		btnOK.setText("OK");
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setLayoutData(new RowData(80, SWT.DEFAULT));
		btnCancel.setText("Cancel");

	}

	public boolean isBtnOkPressed() {
		return btnOkPressed;
	}

	public void init(StatementRule statementRule,
			POJOIndependenceSolver pojoIndependenceSolver) {
		initialized = false;
		this.statementRule = statementRule;
		this.pojoIndependeceSolver = pojoIndependenceSolver;
		this.predicateNames = new ArrayList<String>();
		List<Predicate> predicatesList = this.pojoIndependeceSolver.getPredicatesList();
		for(Predicate predicate : predicatesList)
			predicateNames.add(predicate.getName());
		
			
		initialized = true;
	}
	

	private void createFakeContexts() {
		this.predicateNames = new ArrayList<String>();
		this.predicateNames.add("1");
		this.predicateNames.add("2");
	}

	public StatementRule getModifiedStatementRule() {
		return this.statementRule;
	}
}

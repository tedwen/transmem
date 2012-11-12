package com.transmem.action;

/**
 * ActionFactory - class to create action objects.
 *
 * Create an object that implements the IAction interface.
 *
 * @verion 0.1
 * @author Ted Wen
 * @date Jan. 2007
 */
public abstract class ActionFactory
{
	/**
	 * Static method to create an action.
	 * The object is created using introspection, so the action class
	 * name should include the package.
	 *
	 * @param actionClass - full class name of the action
	 * @return Action object with IAction interface
	 */
	public static IAction createAction(String actionClass)
	{
		Class actionObject = null;
		IAction action = null;
		try
		{
			if (actionClass.indexOf('.')<0)
				actionClass = "com.transmem.action."+actionClass;
			actionObject = Class.forName(actionClass);
			action = (IAction)actionObject.newInstance();
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
			//log
		}
		return action;
	}
}

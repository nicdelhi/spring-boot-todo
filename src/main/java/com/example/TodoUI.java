package com.example;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@SpringUI
@Theme("valo")
public class TodoUI extends UI {

	private VerticalLayout layout;

	@Autowired
	TodoList todoList;
	String selectedProduct;
	String date;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setupLayout();
		addHeader();
		addForm();
		//addTodoList();
		//addActionButtons();
	}

	private void setupLayout() {
		layout = new VerticalLayout();
		layout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setContent(layout);
	}

	private void addHeader() {
		Label header = new Label("Submit Task");
		header.addStyleName(ValoTheme.LABEL_H1);
		layout.addComponent(header);

	}

	private void addForm() {
		//HorizontalLayout formLayout = new HorizontalLayout();
		//formLayout.setWidth("80%");

		TextField taskField = new TextField();
		taskField.setCaption("Enter taskId");
		taskField.focus();
		//      Button addButton = new Button("");

		//formLayout.addComponentsAndExpand(taskField);
		layout.addComponent(taskField);
		DateField dateFiled = new DateField();
		dateFiled.setCaption("Enter date");
		layout.addComponent(dateFiled);
		// formLayout.addComponent(addButton);
		ComboBox<String> combo = new ComboBox<String>();

		//Add multiple items
		combo.setCaption("Enter your product");
		combo.setItems( "Product1", "Product2", "Product3");
		combo.addValueChangeListener(event -> {
			if (event.getValue() == null) {
				selectedProduct = "";
			} else {
				selectedProduct = event.getValue();
			}

		});
		layout.addComponent(combo);
		//layout.addComponent(formLayout);
		Button submitButton = new Button("Submit task");

		submitButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String   json = "{\"variables\": { \"product\": {\"value\":\""+  selectedProduct + "\"," 
						+ "\"type\": \"String\"},"
						+ "\"date\": { \"value\":\"" + dateFiled.getValue() + "\","
						+ "\"type\": \"String\"}}}";
				System.out.println(json);

				RestTemplate restTemplate = new RestTemplate();

				String url = "http://localhost:8090/engine-rest/task/" + taskField.getValue() + "/complete";
				System.out.println(url);	 
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<String> entity = new HttpEntity<String>(json,headers);
				String response = restTemplate.postForObject(url, entity, String.class);
				System.out.println(response);
				return;
			}});

		layout.addComponent(submitButton);

		/*        addButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
        addButton.setIcon(VaadinIcons.PLUS);

        addButton.addClickListener(click -> {
            todoList.addTodo(new Todo(taskField.getValue()));
            taskField.setValue("");
            taskField.focus();
        });
        addButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);*/
	}

	/*
	 * private void addTodoList() { layout.addComponent(todoList); }
	 */

	/*
	 * private void addActionButtons() { Button deleteButton = new
	 * Button("Submit task");
	 * 
	 * deleteButton.addClickListener(click->todoList.deleteCompleted());
	 * 
	 * layout.addComponent(deleteButton);
	 * 
	 * }
	 */
}

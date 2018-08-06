package dev.slonsky.staffcontrol;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Slon on 12.08.2017.
 */

public class EmployeeAutocompleteAdapter extends ArrayAdapter<EmployeeAutocompleteAdapter.Employee> {

    public static class Employee {
        public String name;
        public String position;

        public Employee(String name, String position) {
            this.name = name;
            this.position = position;
        }
    }

    private Context context;
    private List<Employee> employees;
    private List<Employee> employeesSuggestion;
    private List<Employee> employeesAll;

    private int resource;

    public EmployeeAutocompleteAdapter(@NonNull Context context, @LayoutRes int resource, List<Employee> employees) {
        super(context, resource);
        this.context = context;
        this.employees = employees;
        this.resource = resource;
        employeesSuggestion = new ArrayList<>();
        employeesAll = new ArrayList<>(employees);
    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Nullable
    @Override
    public Employee getItem(int position) {
        return employees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        Employee employee = employees.get(position);
        ((TextView)convertView.findViewById(R.id.name)).setText(employee.name);
        ((TextView)convertView.findViewById(R.id.position)).setText(employee.position);

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return ((Employee)resultValue).name;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint != null) {
                    employeesSuggestion.clear();

                    for(Employee e: employeesAll) {
                        String[] parts = e.name.split(" ");
                        for (String s: parts) {
                            if (s.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                employeesSuggestion.add(e);
                                break;
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = employeesSuggestion;
                    filterResults.count = employeesSuggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                employees.clear();
                if(results != null && results.count > 0) {
                    List<?> result = (List<?>)results.values;
                    for(Object object: result) {
                        if(object instanceof Employee) {
                            employees.add((Employee) object);
                        }
                    }
                } else if(constraint == null) {
                    employees.addAll(employeesAll);
                }
                notifyDataSetChanged();
            }
        };
    }
}

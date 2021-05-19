package view;

import model.MyDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommuteVsShelterCostGUI extends Tool implements ActionListener {
    
    private JLabel titleLabel = new JLabel(new ImageIcon("img/shelterCostVsCommonCommuteTypeLabel.png"));
    private JLabel currentDataDisplay = new JLabel("the current city being displayed");
    private JLabel buttonTitleLabel = new JLabel("Select City for Data Display");
    
    // TODO call MyDataset.getCities() to get the names of all the cities instead
    private JButton markhamButton = new JButton("Markham");
    private JButton vaughnButton = new JButton("Vaughn");
    private JButton richmondButton = new JButton("Richmond Hill");
    private JButton auroraButton = new JButton("Aurora");
    private JButton newMarketButton = new JButton("Newmarket");
    
    public CommuteVsShelterCostGUI () {
        
        setLayout(null);
        
        markhamButton.setFont(new Font("Tahoma",Font.PLAIN, 15));
        vaughnButton.setFont(new Font("Tahoma",Font.PLAIN, 15));
        richmondButton.setFont(new Font("Tahoma",Font.PLAIN, 15));
        auroraButton.setFont(new Font("Tahoma",Font.PLAIN, 15));
        newMarketButton.setFont(new Font("Tahoma",Font.PLAIN, 15));
        
        getBackButton().setBounds(1290, 25, 44, 44);
        add(getBackButton());
        
        titleLabel.setBounds(0, 0, 1366, 768);
        add(titleLabel);
        
        add(getBackButton());
        
        buttonTitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 25));
        buttonTitleLabel.setBounds(455, 500, 150, 35);
        add(buttonTitleLabel);
        
        markhamButton.setBounds(200, 550, 100, 20);
        vaughnButton.setBounds(320, 550, 100, 20);
        richmondButton.setBounds(440, 550, 100, 20);
        auroraButton.setBounds(560, 550, 100, 20);
        newMarketButton.setBounds(380, 600, 100, 20);
        
        setVisible(true);
    }
    
    @Override
    public void initializeDataToDisplay (MyDataset dataset, String groupName) {
        // TODO since this requires two groups at once, the first
        //  you need another displayedData field
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
    }
    
}

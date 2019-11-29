//
//  TabBarController.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/29/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit
import MapKit

class TabBarController: UITabBarController {
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        delegate = self
        
        // Create Tab one
        let viewController = ViewController()
        let tableViewDelegate = TableViewDelegate2()
        let tabOneBarItem = UITabBarItem(title: "List", image: .add, selectedImage: .add)
        tableViewDelegate.viewController = viewController
        viewController.tableViewDelegate = tableViewDelegate
        viewController.tabBarItem = tabOneBarItem
        
        
        // Create Tab two
        let viewController2 = MapViewController()
        let tabTwoBarItem2 = UITabBarItem(title: "Map", image: .actions, selectedImage: .actions)
        viewController2.tabBarItem = tabTwoBarItem2
        
        
        viewControllers = [viewController, viewController2]
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

extension TabBarController: UITabBarControllerDelegate {
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        print(viewController)
    }
}

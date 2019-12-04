//
//  PageViewController.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 12/2/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class PageViewController: UIPageViewController {
    let landingPageViewController = LandingPageViewController()
    let descriptionViewController1: DescriptionViewController = {
        $0.descriptionView.imageView.image = UIImage(named: "Graph")
        $0.descriptionView.descriptionTextView.text = "Whether you're fixing up your first home or renovating a fleet of homes across the US, Rinnovation simplifies your decision.\n\nSimply select your city and the renovation you're considering. The graph shows the % of the cost recouped for similar projects. The red circle shows our prediction for 2020."
        return $0
    }(DescriptionViewController())
    let descriptionViewController2: DescriptionViewController = {
        $0.descriptionView.imageView.image = UIImage(named: "Map")
        $0.descriptionView.descriptionTextView.text = "For professional flippers, we built a power feature to help you invest in the right markets.\n\nChoose a renovation you're considering, then when you go to select a city, switch to the Map tab. Now, you can easily browse across the nation to find which market will have the most demand for that renovation in 2020."
        return $0
    }(DescriptionViewController())
    let entranceViewController = EntranceViewController()
    
    override init(transitionStyle style: UIPageViewController.TransitionStyle, navigationOrientation: UIPageViewController.NavigationOrientation, options: [UIPageViewController.OptionsKey : Any]? = nil) {
        super.init(transitionStyle: .scroll, navigationOrientation: navigationOrientation, options: options)
        dataSource = self
        setViewControllers([landingPageViewController], direction: .forward, animated: true)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        guard let view = view else { return }
        view.subviews.forEach {
            switch $0 {
            case let scrollView as UIScrollView: scrollView.frame = view.bounds
            case let pageControl as UIPageControl: view.bringSubviewToFront(pageControl)
            default: break
            }
        }
    }
}

// MARK: - UIPageViewControllerDataSource

extension PageViewController: UIPageViewControllerDataSource {
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        switch viewController {
        case descriptionViewController1: return landingPageViewController
        case descriptionViewController2: return descriptionViewController1
        case entranceViewController: return descriptionViewController2
        default: return nil
        }
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        switch viewController {
        case landingPageViewController: return descriptionViewController1
        case descriptionViewController1: return descriptionViewController2
        case descriptionViewController2: return entranceViewController
        default: return nil
        }
    }
    
    func presentationCount(for pageViewController: UIPageViewController) -> Int {
        return 4
    }
    
    func presentationIndex(for pageViewController: UIPageViewController) -> Int {
        switch viewControllers?.first {
        case landingPageViewController?: return 0
        case descriptionViewController1?: return 1
        case descriptionViewController2?: return 2
        case entranceViewController?: return 3
        default: fatalError()
        }
    }
}

//
//  MapView.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 11/29/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import MapKit

class MapView: UIView {
    let mapView = MKMapView()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        addSubview(mapView)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - UIView

extension MapView {
    override func layoutSubviews() {
        super.layoutSubviews()
        mapView.frame = frame
    }
}

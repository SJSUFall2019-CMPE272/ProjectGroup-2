//
//  LandingPageView.swift
//  Rinnovation
//
//  Created by Andrew Selvia on 12/3/19.
//  Copyright © 2019 Rinnovation. All rights reserved.
//

import UIKit

class LandingPageView: UIView {
    let iconImageView: UIImageView = {
        $0.layer.cornerRadius = 25
        $0.clipsToBounds = true
        return $0
    }(UIImageView(image: UIImage(named: "Icon")!))
    let titleLabel: UILabel = {
        $0.text = "Rinnovation"
        $0.font = .systemFont(ofSize: 50)
        return $0
    }(UILabel())
    let subtitleLabel: UILabel = {
        $0.text = "Innovate your home renovation."
        $0.font = .systemFont(ofSize: 22)
        return $0
    }(UILabel())
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor.white.withAlphaComponent(0.7)
        addSubview(iconImageView)
        addSubview(titleLabel)
        addSubview(subtitleLabel)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}

// MARK: - UIView

extension LandingPageView {
    override func layoutSubviews() {
        super.layoutSubviews()
        titleLabel.sizeToFit()
        subtitleLabel.sizeToFit()
        titleLabel.center = center
        subtitleLabel.center = center
        let offset = subtitleLabel.frame.height / 2 + 8
        titleLabel.frame.origin.y -= offset
        subtitleLabel.frame.origin.y += offset
        
        let iconImageViewSideLength = frame.width * 0.55
        iconImageView.frame.size = CGSize(width: iconImageViewSideLength, height: iconImageViewSideLength)
        iconImageView.center = center
        iconImageView.frame.origin.y = titleLabel.frame.minY - 16 - iconImageViewSideLength
    }
}

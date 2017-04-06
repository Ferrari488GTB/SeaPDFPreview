//
//  PDFViewController.h
//  HelloIOS
//
//  Created by administrator on 2017/3/29.
//  Copyright © 2017年 administrator. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PDFViewController : UIViewController<UIWebViewDelegate>

{
    NSString *_type;
    NSString *_filePath;
    NSString *_fileName;
    NSURL * _url;
    NSURLRequest *_request;
    UIWebView *_pdfWebView;
    UIView *_maskView;
    UIActivityIndicatorView *_indicatorView;
    UIButton *_closeBtn;
}

//隐藏顶部状态栏
-(BOOL)prefersStatusBarHidden;
-(instancetype)initWithType:(NSString *)type filePath:(NSString *)filePath fileName:(NSString *)fileName;
-(void)viewDidLoad;
-(void)startAnimate;
-(void)endAnimate;
-(void)appendBtns;
-(void)closePDFPreview;

@end

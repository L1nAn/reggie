package com.mzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mzw.entity.AddressBook;
import com.mzw.mapper.AddressBookMapper;
import com.mzw.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author :马治伟
 * @version :1.0
 * @Date : 2023/4/27
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}

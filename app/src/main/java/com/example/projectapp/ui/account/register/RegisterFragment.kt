package com.example.projectapp.ui.account.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.projectapp.R
import com.example.projectapp.databinding.FragmentRegisterBinding
import com.example.projectapp.network.getNetworkService
import com.example.projectapp.repository.UserRepository
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels(
            factoryProducer = {
                RegisterViewModel.FACTORY(UserRepository(getNetworkService()))
            }
    )

    //private var adapter: RegisterTabsAdapter? = null
    private val tabsTitle: MutableList<String> = ArrayList()
    private val fragments: MutableList<Fragment> = ArrayList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // adapter = RegisterTabsAdapter(this, fragments)
        //  binding.registerViewpager.setAdapter(adapter)
        // TabLayoutMediator(binding.registerTabs, binding.registerViewpager, TabConfigurationStrategy { tab, position -> tab.text = tabsTitle[position] }).attach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //  val accountLabel = RegisterFragmentArgs.fromBundle(arguments!!).accountLabel
        //  if (accountLabel == getString(R.string.admin_account)) {
        //tabsTitle.add("Personal Data")
        //tabsTitle.add("Company Data")
        //fragments.add(DataFragment())
        // } else if (accountLabel == getString(R.string.user_account)) {
        // tabsTitle.add("Personal Data")
        //fragments.add(DataFragment())
        //  }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.createbtn.setOnClickListener { view: View? ->
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            viewModel.onCreateNewAccountBtnClicked(name, email, password)
        }

        viewModel.userProperty.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                Toast.makeText(context, it.name.toString(), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "false", Toast.LENGTH_LONG).show()
            }
        } )

        return binding.root
    }

    /*  internal inner class RegisterTabsAdapter(fragment: Fragment, private val mFragments: List<Fragment>) : FragmentStateAdapter(fragment) {
          override fun createFragment(position: Int): Fragment {
              // Return a NEW fragment instance in createFragment(int)
              //    Bundle args = new Bundle();
              // Our object is just an integer :-P
              //   args.putInt(RegisterFragmentAdminPersonalData.ARG_OBJECT, position + 1);
              //  fragment.setArguments(args);
              return mFragments[position]
          }

          override fun getItemCount(): Int {
              return mFragments.size
          }

      }*/
}
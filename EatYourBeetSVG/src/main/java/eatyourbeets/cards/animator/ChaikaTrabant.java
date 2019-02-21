package eatyourbeets.cards.animator;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_SavableInteger;
import eatyourbeets.cards.Synergies;

public class ChaikaTrabant extends AnimatorCard_SavableInteger implements CustomSavable<Integer>
{
    public static final String ID = CreateFullID(ChaikaTrabant.class.getSimpleName());

    private final static String[] KeyWords = new String[3];

    private final String BASE_DESCRIPTION;

    public ChaikaTrabant()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(7,0);

        this.BASE_DESCRIPTION = this.rawDescription;
        AddExtendedDescription();

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        UpdateBattleDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));

        if (secondaryValue == 0)
        {
            GameActionsHelper.ApplyPower(p, m, new WeakPower(m, 1, false), 1);
        }
        else if (secondaryValue == 1)
        {
            GameActionsHelper.AddToBottom(new StunMonsterAction(m, p));
        }
        else if (secondaryValue == 2)
        {
            GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, 1, false), 1);
        }

        Rotate();
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(5);
        }
    }

    private void Rotate()
    {
        String keyword = "[#EFC851]" + cardStrings.EXTENDED_DESCRIPTION[0] + "[]";

        int newValue = (this.secondaryValue + 1) % KeyWords.length;
        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
        {
            ((ChaikaTrabant)c).SetValue(newValue);
            ((ChaikaTrabant)c).UpdateBattleDescription();
        }

        AbstractCard chaika = GetMasterDeckInstance();
        if (chaika != null)
        {
            ((ChaikaTrabant)chaika).SetValue(newValue);
        }
    }

    private void UpdateBattleDescription()
    {
        logger.info(BASE_DESCRIPTION);
        logger.info(KeyWords[0]);
        logger.info(KeyWords[1]);
        logger.info(KeyWords[2]);
        String keyword = "[#EFC851]" + cardStrings.EXTENDED_DESCRIPTION[0] + "[]";
        rawDescription = BASE_DESCRIPTION.replace(keyword, KeyWords[secondaryValue]);
        initializeDescription();
    }

    private static String GetKeywordName(String powerName)
    {
        String s = GameDictionary.parentWord.get(powerName.toLowerCase());
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    static
    {
        KeyWords[0] = GetKeywordName(WeakPower.NAME);
        KeyWords[1] = GetKeywordName(StunMonsterPower.POWER_ID);
        KeyWords[2] = GetKeywordName(VulnerablePower.NAME);
    }
}
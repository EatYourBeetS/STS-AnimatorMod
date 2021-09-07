package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Enju_Barasuishou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Enju_Barasuishou.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Elemental).SetSeries(CardSeries.RozenMaiden);
    static
    {
        DATA.AddPreview(new Crystallize(), false);
    }

    public Enju_Barasuishou(boolean Upgd)  // only for preview if Hitcount = -1
    {
        this(-1);
        if (Upgd)
            upgrade();
    }

    public Enju_Barasuishou()
    {
        this(0);
    }

    public Enju_Barasuishou(int Hitcount) // for use
    {
        super(DATA);

        Initialize(6, 0, Hitcount);
        SetUpgrade(2, 0, 0);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        
        SetUnique(true, true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (magicNumber >= 0)
            return super.GetDamageInfo().AddMultiplier(magicNumber);
        else
            return super.GetDamageInfo().AddSuffix("xX");
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this).SetText("3", Settings.CREAM_COLOR);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(3);

        if (magicNumber < 0)
        {
            throw new RuntimeException("This is not supposed to be played.");
        }

        for (int i = 0; i < magicNumber; i ++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                    .SetVFX(true, false);
        }

        GameActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0 && !GameUtilities.IsHindrance(cards.get(0)))
            {
                GameActions.Bottom.ModifyAllInstances(uuid, AbstractCard::upgrade);
                this.flash();
            }
        });
    }
}


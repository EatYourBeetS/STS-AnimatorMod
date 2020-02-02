package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class AcuraTooru extends AnimatorCard
{
    public static final String ID = Register(AcuraTooru.class);
    static
    {
        GetStaticData(ID).InitializePreview(ThrowingKnife.GetCardForPreview(), false);
    }

    public AcuraTooru()
    {
        super(ID, 2, CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(4, 0, 4, 2);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainBlock(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.CreateThrowingKnives(secondaryValue);

        if (HasSynergy())
        {
            GameActions.Bottom.GainBlock(magicNumber);
        }
    }
}
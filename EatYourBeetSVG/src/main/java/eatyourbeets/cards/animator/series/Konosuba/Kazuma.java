package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.utilities.GameActions;

public class Kazuma extends AnimatorCard
{
    public static final String ID = Register_Old(Kazuma.class);

    public Kazuma()
    {
        super(ID, 1, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF);

        Initialize(0, 6, 4);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (HasSynergy())
        {
            return DamageAttribute.Instance.SetCard(this).SetText(String.valueOf(magicNumber), Settings.CREAM_COLOR).SetIconTag("???");
        }

        return null;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.Cycle(name, 1);

        if (HasSynergy())
        {
            GameActions.Bottom.DealDamageToRandomEnemy(magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }
}
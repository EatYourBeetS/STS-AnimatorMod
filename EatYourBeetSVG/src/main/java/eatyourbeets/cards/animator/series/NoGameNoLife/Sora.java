package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.SoraAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.SoraEffects.SoraEffect;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import patches.AbstractEnums;

public class Sora extends AnimatorCard
{
    public static final String ID = Register(Sora.class.getSimpleName(), EYBCardBadge.Special);

    public final SoraEffect effect;

    public Sora(SoraEffect effect, String name, String description)
    {
        super(staticCardData.get(ID), ID + "Alt", AnimatorResources.GetCardImage(ID + "Alt"),
                0, CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, CardRarity.RARE, CardTarget.ALL);

        this.name = name;
        this.effect = effect;
        this.cardText.OverrideDescription(description, "-", true);
        //this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
    }

    public Sora()
    {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0,0, 2);

        this.effect = null;

        SetMultiDamage(true);
        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractCard c : p.drawPile.group)
        {
            if (Shiro.ID.equals(c.cardID))
            {
                GameActions.Top.MoveCard(c, p.hand, p.drawPile);
                break;
            }
        }

        GameActions.Bottom.Add(new WaitAction(0.4f));
        GameActions.Bottom.Add(new SoraAction(name, magicNumber));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return effect == null && super.canUpgrade();
    }
}
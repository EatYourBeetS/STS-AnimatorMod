package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.BlizzardEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Cirno extends AnimatorCard
{
    public static final String ID = Register(Cirno.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Cirno()
    {
        super(ID, 1, CardType.ATTACK, AbstractCard.CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(6,0);
        SetMultiDamage(true);
        SetSynergy(Synergies.TouhouProject);
        SetEthereal(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new BlizzardEffect(1, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.6f);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.ChannelOrb(new Frost(), true);
    }
    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        GameActions.Bottom.ChannelOrb(new Frost(), true);
    }
    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }


}


package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;

public class ShichikaKyotouryuu extends AnimatorCard implements MartialArtist
{
    public static final String ID = Register_Old(ShichikaKyotouryuu.class);

    public ShichikaKyotouryuu()
    {
        super(ID, 1, CardRarity.SPECIAL, CardType.ATTACK, CardTarget.ENEMY);

        Initialize(1, 0, 4);
        SetUpgrade(1, 0, 0);
        SetScaling(0, 2, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new FlashAtkImgEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale,
        AbstractGameAction.AttackEffect.SLASH_HEAVY), 0.1F);

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

//        if (upgraded)
//        {
//            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
//        }

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        GameActions.Bottom.VFX(new FlashAtkImgEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale,
        AbstractGameAction.AttackEffect.SLASH_HEAVY), 0.1F);

        if (HasSynergy())
        {
            GameActions.Bottom.GainForce(1);
        }
    }
}
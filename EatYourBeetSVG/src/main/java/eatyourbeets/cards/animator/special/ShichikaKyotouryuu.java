package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class ShichikaKyotouryuu extends AnimatorCard implements MartialArtist, Hidden
{
    public static final String ID = Register(ShichikaKyotouryuu.class.getSimpleName(), EYBCardBadge.Synergy);

    public ShichikaKyotouryuu()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.SPECIAL, CardTarget.ENEMY);

        Initialize(1, 0, 4);
        SetUpgrade(1, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + MartialArtist.GetScaling());
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

        if (HasActiveSynergy())
        {
            GameActions.Bottom.GainForce(1);
        }
    }
}
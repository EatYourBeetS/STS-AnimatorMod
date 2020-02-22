package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.MartialArtist;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;

public class ShichikaKyotouryuu extends AnimatorCard implements MartialArtist
{
    public static final EYBCardData DATA = Register(ShichikaKyotouryuu.class).SetAttack(1, CardRarity.SPECIAL);

    public ShichikaKyotouryuu()
    {
        super(DATA);

        Initialize(1, 0, 4);
        SetUpgrade(1, 0, 0);
        SetScaling(0, 1, 1);

        SetExhaust(true);
        SetSynergy(Synergies.Katanagatari);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Add(new RemoveAllBlockAction(m, player));
//        GameActions.Bottom.VFX(new FlashAtkImgEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale,
//        AbstractGameAction.AttackEffect.SLASH_HEAVY), 0.1F);

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
            ForcePower.PreserveOnce();
            AgilityPower.PreserveOnce();
        }

        GameActions.Last.Add(new RemoveAllBlockAction(m, player));
    }
}
package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BlizzardEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class Ain extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Ain.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Discard);

    public Ain()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(1, 0, 3, 1);

        SetMultiDamage(true);
        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainIntellect(secondaryValue);
        }
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + Spellcaster.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new BlizzardEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), Settings.FAST_MODE ? 0.25f : 1.0F);

        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainIntellect(secondaryValue);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}
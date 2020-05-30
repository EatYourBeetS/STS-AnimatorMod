package eatyourbeets.cards.animator.beta.AngelBeats;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;
import eatyourbeets.utilities.GameActions;

public class AngelAlter extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(AngelAlter.class).SetAttack(0, CardRarity.SPECIAL, EYBAttackType.Piercing).SetColor(CardColor.COLORLESS);

    public AngelAlter()
    {
        super(DATA);

        Initialize(10, 0, 1, 0);
        SetScaling(0, 2, 2);

        SetSynergy(Synergies.AngelBeats);
        CardModifierManager.addModifier(this, new AfterLifeMod());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        GameActions.Bottom.GainForce(magicNumber, true);
        GameActions.Bottom.GainAgility(magicNumber, true);

        GameActions.Bottom.MakeCardInDrawPile(this).SetUpgrade(false, true);
        GameActions.Bottom.MakeCardInDiscardPile(this).SetUpgrade(false, true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetScaling(0, 3, 3);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        boolean playable = super.cardPlayable(m);
        if (playable && !isInAutoplay)
        {
            if (checkPlayable() && CardModifierManager.hasModifier(this, AfterLifeMod.ID)) {
                return true;
            } else {
                return false;
            }
        }

        return playable;
    }

    private boolean checkPlayable() {
        for (ControllableCard controllableCard : CombatStats.ControlPile.controllers) {
            if (controllableCard.card.equals(this)) {
                return true;
            }
        }
        return false;
    }
}
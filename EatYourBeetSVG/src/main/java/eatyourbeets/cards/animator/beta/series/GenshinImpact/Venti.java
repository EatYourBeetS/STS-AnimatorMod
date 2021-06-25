package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Venti extends AnimatorCard {
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);

    public Venti() {
        super(DATA);

        Initialize(0, 0, 2, 1);
        SetUpgrade(0, 0, 0);

        SetEthereal(true);
        SetShapeshifter();
        SetSynergy(Synergies.GenshinImpact);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Bottom.Draw(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);

        for (int i = 0; i < magicNumber; i++)
        {
            boolean shouldEvoke = p.filledOrbCount() > 0 && p.maxOrbs - p.filledOrbCount() == 0;
            if (shouldEvoke) {
                AbstractOrb orb = p.orbs.get(0);
                if (!(orb instanceof EmptyOrbSlot)) {
                    GameActions.Bottom.Callback(orb, (orb_, __) ->
                    {
                        ((AbstractOrb) orb_).triggerEvokeAnimation();
                        ((AbstractOrb) orb_).onEvoke();
                    });
                }
            }
            GameActions.Bottom.ChannelOrb(new Aether());
        }

    }
}
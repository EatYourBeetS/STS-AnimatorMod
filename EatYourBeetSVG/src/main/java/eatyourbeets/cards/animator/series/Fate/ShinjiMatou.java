package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.animator.special.ShinjiMatou_CommandSpell;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashSet;

public class ShinjiMatou extends AnimatorCard
{
    private static final HashSet<CardType> cardTypes = new HashSet<>();

    public static final EYBCardData DATA = Register(ShinjiMatou.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Random);
    static
    {
        DATA.AddPreview(new ShinjiMatou_CommandSpell(), false);
    }

    public ShinjiMatou()
    {
        super(DATA);

        Initialize(0, 2, 4);
        SetUpgrade(0, 0, 2);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block)
        .SetVFX(true, true);

        GameActions.Bottom.Callback(() ->
        {
            AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
            if (GameUtilities.IsValidTarget(enemy))
            {
                GameActions.Top.ApplyPoison(player, enemy, magicNumber);
                GameActions.Top.VFX(new PotionBounceEffect(player.hb.cX, player.hb.cY, enemy.hb.cX, enemy.hb.cY), 0.3f);
            }
        });

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.MakeCardInHand(new ShinjiMatou_CommandSpell())
            .AddCallback(c -> c.retain = true);
        }
    }
}

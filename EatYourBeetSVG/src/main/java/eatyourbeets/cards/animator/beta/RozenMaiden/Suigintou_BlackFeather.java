package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Suigintou_BlackFeather extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Suigintou_BlackFeather.class)
    		.SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.Random);

    public Suigintou_BlackFeather()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);

        SetPurge(true);
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Top.Purge(uuid);

        GameActions.Bottom.Callback(() ->
        {
            AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
            GameActions.Top.ReduceStrength(enemy, magicNumber, true);
            GameActions.Top.VFX(new ThrowDaggerEffect(enemy.hb.cX, enemy.hb.cY));
        });
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false)
                .SetOptions(false, false, false)
                .AddCallback(cards ->
                {
                    if (cards != null && cards.size() > 0)
                        GameActions.Bottom.Add(new CreateRandomCurses(1, p.hand));
                });
    }
        /*
        AbstractMonster enemy1 = GameUtilities.GetRandomEnemy(true);
        GameActions.Bottom.ApplyVulnerable(player, enemy1, magicNumber);

        AbstractMonster enemy2 = GameUtilities.GetRandomEnemy(true);
        GameActions.Bottom.ApplyWeak(player, enemy2, magicNumber);

        if (upgraded)
        {
            AbstractMonster enemy3 = GameUtilities.GetRandomEnemy(true);
            GameActions.Bottom.ReduceStrength(enemy3, magicNumber, true);
        }

        GameActions.Bottom.Add(new CreateRandomCurses(1, player.hand));

         */
}

package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.animator.beta.series.RozenMaiden.Suigintou;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Suigintou_BlackFeather extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Suigintou_BlackFeather.class)
    		.SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.Random).SetSeries(CardSeries.RozenMaiden)
            .PostInitialize(data -> data.AddPreview(new Suigintou(), false));

    public Suigintou_BlackFeather()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 0);
        SetAffinity_Water(2, 0, 0);
        SetAffinity_Dark(2, 0, 0);

        SetPurge(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnManualDiscard()
    {

        GameActions.Bottom.Callback(() ->
        {
            AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);
            GameActions.Top.ReduceStrength(enemy, magicNumber, true);
            GameActions.Top.VFX(new ThrowDaggerEffect(enemy.hb.cX, enemy.hb.cY));
        });
        GameActions.Last.Purge(uuid);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() ->
        {
            if (!DrawSuigintou(player.drawPile))
            {
                DrawSuigintou(player.discardPile);
            }
        });

         GameActions.Bottom.Add(new CreateRandomCurses(1, p.hand));
    }

    private boolean DrawSuigintou(CardGroup group) // Copied from Chibimoth.java
    {
        for (AbstractCard c : group.group)
        {
            if (Suigintou.DATA.ID.equals(c.cardID))
            {
                if (group.type != CardGroup.CardGroupType.HAND)
                {
                    GameEffects.List.ShowCardBriefly(makeStatEquivalentCopy());

                    if (upgraded) {
                        GameActions.Top.MoveCard(c, group, player.hand)
                                .ShowEffect(true, true)
                                .AddCallback(GameUtilities::Retain);
                    }
                    else {
                        GameActions.Top.MoveCard(c, group, player.hand)
                                .ShowEffect(true, true);
                    }
                }

                return true;
            }
        }

        return false;
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

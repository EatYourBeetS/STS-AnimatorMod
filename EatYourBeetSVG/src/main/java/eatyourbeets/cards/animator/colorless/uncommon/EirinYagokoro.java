package eatyourbeets.cards.animator.colorless.uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Sozu;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EirinYagokoro extends AnimatorCard implements OnAddToDeckListener
{
    public static final EYBCardData DATA = Register(EirinYagokoro.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject);

    public EirinYagokoro()
    {
        super(DATA);

        Initialize(0, 0, 6, 5);
        SetUpgrade(0, 0, 2, 2);

        SetAffinity_Dark(1);
        SetAffinity_Blue(1);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectCreature(SelectCreature.Targeting.Any, name)
        .IsCancellable(false)
        .AddCallback(c ->
        {
            if (c.isPlayer)
            {
                GameActions.Bottom.VFX(VFX.PotionBounce(hb, c.hb).SetColor(new Color(1f, 0.3f, 0.3f, 0f)), 0.75f, true);
                GameActions.Bottom.HealPlayerLimited(this, secondaryValue);
            }
            else
            {
                GameActions.Bottom.VFX(VFX.PotionBounce(hb, c.hb).SetColor(new Color(0.4f, 1f, 0f, 0f)), 0.75f, true);
                GameActions.Bottom.ApplyPoison(player, c, magicNumber);
            }
        });
    }

    @Override
    public boolean OnAddToDeck()
    {
        final AbstractRelic sozu = GameUtilities.GetRelic(Sozu.ID);
        if (sozu == null)
        {
            AbstractDungeon.player.obtainPotion(AbstractDungeon.returnRandomPotion(false));
        }
        else
        {
            sozu.flash();
        }

        return true;
    }
}
package eatyourbeets.screens.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.interfaces.csharp.ActionT0;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.screens.AbstractScreen;
import eatyourbeets.screens.controls.GenericButton;
import eatyourbeets.screens.controls.GridLayoutControl;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeriesSelectionScreen extends AbstractScreen
{
    private static final Random rng = new Random();

    private int totalCards = 0;
    private final Map<AbstractCard, AnimatorLoadout> loadoutMap = new HashMap<>();
    private final ArrayList<AnimatorLoadout> selectedLoadouts = new ArrayList<>();

    private final GenericButton deselectAll = CreateButton(Settings.WIDTH * 0.82f, Settings.HEIGHT * 0.68f, "Deselect All", null).SetColor(Color.FIREBRICK);
    private final GenericButton selectRandom75 = CreateButton(Settings.WIDTH * 0.82f, Settings.HEIGHT * 0.60f, "Random (75 cards)", null).SetColor(Color.SKY);
    private final GenericButton selectRandom100 = CreateButton(Settings.WIDTH * 0.82f, Settings.HEIGHT * 0.52f, "Random (100 cards)", null).SetColor(Color.SKY);
    private final GenericButton selectAll = CreateButton(Settings.WIDTH * 0.82f, Settings.HEIGHT * 0.44f, "Select All", null).SetColor(Color.ROYAL);
    private final GenericButton confirm = CreateButton(Settings.WIDTH * 0.82f, Settings.HEIGHT * 0.25f, "Confirm", null).SetColor(Color.FOREST);

    private final GridLayoutControl gridLayoutControl = new GridLayoutControl();

    public SeriesSelectionScreen()
    {
        deselectAll.hb.resize(Settings.WIDTH * 0.18f, Settings.HEIGHT * 0.07f);
        selectRandom75.hb.resize(Settings.WIDTH * 0.18f, Settings.HEIGHT * 0.07f);
        selectRandom100.hb.resize(Settings.WIDTH * 0.18f, Settings.HEIGHT * 0.07f);
        selectAll.hb.resize(Settings.WIDTH * 0.18f, Settings.HEIGHT * 0.07f);
        confirm.hb.resize(Settings.WIDTH * 0.18f, Settings.HEIGHT * 0.08f);

        confirm.isDisabled = true;

        selectAll.SetOnClick(this::SelectAll);
        deselectAll.SetOnClick(this::DeselectAll);
        selectRandom75.SetOnClick(() -> SelectRandom(75));
        selectRandom100.SetOnClick(() -> SelectRandom(100));
    }

    public void Open(boolean firstTime)
    {
        super.Open();

        loadoutMap.clear();
        selectedLoadouts.clear();
        totalCards = 0;

        CardGroup test = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        RandomizedList<AnimatorLoadout> loadouts = new RandomizedList<>(GR.Animator.Database.BaseLoadouts);
        AnimatorLoadout loadout = GR.Animator.Database.SelectedLoadout;

        loadouts.Remove(loadout);

        int i = 0;
        while (loadouts.Count() >= 0)
        {
            if (loadout != null)
            {
                int size = loadout.GetNonColorlessCards().size();
                if (size > 0)
                {
                    i+=1;

                    AbstractCard card = CardLibrary.getCard(loadout.GetSymbolicCardID());
                    AbstractCard.CardRarity rarity = i <= 3 ? AbstractCard.CardRarity.RARE : AbstractCard.CardRarity.COMMON;
                    AnimatorCardBuilder builder = new AnimatorCardBuilder(String.valueOf(loadout.ID)).SetImage(card.assetUrl)
                    .SetProperties(card.type, rarity, AbstractCard.CardTarget.NONE)
                    .SetText(loadout.Name, "Contains " + size + " cards from this series.", "");

                    card = builder.Build();
                    loadoutMap.put(card, loadout);

                    if (i <= 3)
                    {
                        card.retain = true;
                        Select(card);
                    }
                    else
                    {
                        card.retain = false;
                        Deselect(card);
                    }

                    test.addToTop(card);
                }
            }

            loadout = loadouts.Retrieve(GR.Common.DungeonData.GetRNG());
        }

        gridLayoutControl.Open(test, this::OnCardClicked);
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        gridLayoutControl.Render(sb);

        deselectAll.Render(sb);
        selectRandom75.Render(sb);
        selectRandom100.Render(sb);
        selectAll.Render(sb);
        confirm.Render(sb);
    }

    @Override
    public void Update()
    {
        deselectAll.Update();
        selectRandom75.Update();
        selectRandom100.Update();
        selectAll.Update();
        confirm.Update();

        gridLayoutControl.Update();
    }

    private void OnCardClicked(AbstractCard card)
    {
        if (card.retain)
        {
            CardCrawlGame.sound.play("CARD_REJECT");
        }
        else
        {
            if (selectedLoadouts.contains(loadoutMap.get(card)))
            {
                Deselect(card);
            }
            else
            {
                Select(card);
                CardCrawlGame.sound.play("CARD_SELECT");
            }
        }
    }

    private void SelectRandom(int minimum)
    {
        int currentCount = 0;

        RandomizedList<AbstractCard> toSelect = new RandomizedList<>();
        for (AbstractCard card : loadoutMap.keySet())
        {
            Deselect(card);

            if (!card.upgraded)
            {
                toSelect.Add(card);
            }
        }

        while (totalCards < minimum)
        {
            Select(toSelect.Retrieve(rng));
        }
    }

    private void DeselectAll()
    {
        for (AbstractCard card : loadoutMap.keySet())
        {
            Deselect(card);
        }
    }

    private void SelectAll()
    {
        for (AbstractCard card : loadoutMap.keySet())
        {
            Select(card);
        }
    }

    private void Deselect(AbstractCard card)
    {
        if (!card.retain)
        {
            card.stopGlowing();
            card.targetTransparency = 0.75f;

            AnimatorLoadout loadout = loadoutMap.get(card);
            if (selectedLoadouts.remove(loadout))
            {
                totalCards -= loadout.GetNonColorlessCards().size();
            }
        }
    }

    private void Select(AbstractCard card)
    {
        if (!card.retain)
        {
            card.beginGlowing();
        }

        AnimatorLoadout loadout = loadoutMap.get(card);
        if (!selectedLoadouts.contains(loadout))
        {
            selectedLoadouts.add(loadout);
            totalCards += loadout.GetNonColorlessCards().size();
        }

        card.targetTransparency = 1f;
    }

    private static GenericButton CreateButton(float x, float y, String text, ActionT0 onClick)
    {
        final Texture buttonTexture = GR.Common.Textures.HexagonalButton;
        final Texture buttonBorderTexture = GR.Common.Textures.HexagonalButtonBorder;

        return new GenericButton(buttonTexture, x, y)
                .SetBorder(buttonBorderTexture, Color.WHITE)
                .SetOnClick(onClick)
                .SetText(text);
    }
}
